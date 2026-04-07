package com.FlashCart.FlashSaleSystem.Service;

import com.FlashCart.FlashSaleSystem.DTOs.FlashSaleDTO;
import com.FlashCart.FlashSaleSystem.DTOs.OrderDTO;
import com.FlashCart.FlashSaleSystem.Enums.OrderStatus;
import com.FlashCart.FlashSaleSystem.Enums.PaymentStatus;
import com.FlashCart.FlashSaleSystem.ErrorControl.APIException;
import com.FlashCart.FlashSaleSystem.ErrorControl.ResourceNotFoundException;
import com.FlashCart.FlashSaleSystem.Models.FlashSale;
import com.FlashCart.FlashSaleSystem.Models.Order;
import com.FlashCart.FlashSaleSystem.Models.User;
import com.FlashCart.FlashSaleSystem.Repository.FlashSaleRepository;
import com.FlashCart.FlashSaleSystem.Repository.OrderRepository;
import com.FlashCart.FlashSaleSystem.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class OrderServiceImpl implements OrderService{
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private FlashSaleRepository flashSaleRepository;
    @Autowired
    private FlashSaleService flashSaleService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RedisLockService redisLockService;

    @Override
    @Transactional
    public OrderDTO purchase(Long saleId, Long userId, Integer quantity) {

        //checking if user exists
        User currUser = userRepository.findById(userId).orElseThrow(()->
                new ResourceNotFoundException("User","userId",userId));

        //check for Active flash sale
        FlashSale sale = flashSaleRepository.findById(saleId).orElseThrow(()->
                new ResourceNotFoundException("flashSale","FlashSaleId",saleId));
        if (sale.getStartAt().isAfter(LocalDateTime.now())||sale.getEndsAt().isBefore(LocalDateTime.now())){
            throw new APIException("Sale with id-"+saleId+" is InActive");
        }

        // adding the redis distributed lock to further process
        String lockKey = "lock:flashSale:"+saleId;
        boolean lockAcquired = redisLockService.acquireLock(lockKey,3000);
        if (!lockAcquired) {
            throw new APIException("Too many requests, please try again");
        }
        try {//critical section

            //check for Idempotency key constraints
            String key = saleId+"_"+userId;
            Order existingOrder = orderRepository.findByIdempotencyKey(key);
            if (existingOrder!=null) throw new APIException("Current User have already ordered From this sale");

            //check for the quantity is available using redis and decrementing the quantity from redis not touching the database
            String redisKey = "FlashSale:Stock" + sale.getSaleId();
            Long remaining = redisService.decreaseStock(redisKey, quantity);
            if (remaining < 0) {
                redisService.increaseStock(redisKey, quantity);
                throw new APIException("Out of Stock!!");
            }
            //if (sale.getSaleStock()<quantity) throw new APIException("Out of Stock!!");
            //create the order
            Order currentOrder = new Order();
            currentOrder.setIdempotencyKey(key);
            currentOrder.setCreatedAt(LocalDateTime.now());
            currentOrder.setPaymentStatus(String.valueOf(PaymentStatus.PENDING));
            currentOrder.setStatus(String.valueOf(OrderStatus.CREATED));
            currentOrder.setUser(currUser);
            currentOrder.setFlashSale(sale);
            //sale.setSaleStock(sale.getSaleStock()-quantity); don't using this, because we are caching the stock in redis and updated there
            currentOrder.setQuantity(quantity);
            currentOrder.setTotalPrice(sale.getSpecialPrice() * quantity);
            //save the order
            Order savedOrder = orderRepository.save(currentOrder);

            //adding time to live for the order if the payment not done for order in stipulated time
            String expiryKey = "order:expiry:" + savedOrder.getOrderId();
            redisService.setWithTTL(expiryKey, "PENDING", 30);

            //return the DTO
            return modelMapper.map(savedOrder, OrderDTO.class);
        }finally {
            //releasing the lock
            redisLockService.releaseLock(lockKey);
        }
    }

    @Override
    @Transactional
    public String pay(Long orderId, Boolean paymentSuccess) {
        //validate the order
        Order currOrder = orderRepository.findById(orderId).orElseThrow(()->
                new ResourceNotFoundException("order","orderId",orderId));
        FlashSale sale = currOrder.getFlashSale();
        //validating order status
        if (!currOrder.getPaymentStatus().equals(PaymentStatus.PENDING.name())) {
            throw new APIException("Payment already processed");
        }
        //creating redis key for stock handling
        String redisKey = "FlashSale:Stock"+sale.getSaleId();
        //if payment is success update the payment and order status in order
        if(paymentSuccess){
            currOrder.setPaymentStatus(String.valueOf(PaymentStatus.SUCCESS));
            currOrder.setStatus(String.valueOf(OrderStatus.SUCCESS));
            orderRepository.save(currOrder);
            //update the stock in the sale database
            sale.setSaleStock(sale.getSaleStock()-currOrder.getQuantity());
            //saving the sale
            flashSaleRepository.save(sale);

            //removing the ttl form the order
            String expiryKey = "order:expiry:" + orderId;
            redisService.delete(expiryKey);

            return "Payment Success";
        }
        //else update the payment and order status and return the stock to the redis
        else {
            currOrder.setPaymentStatus(String.valueOf(PaymentStatus.FAILED));
            currOrder.setStatus(String.valueOf(OrderStatus.CANCELLED));
            //adding the items to the redis again
            redisService.increaseStock(redisKey,currOrder.getQuantity());
            //saving the updated models;
            orderRepository.save(currOrder);

            return "Order cancelled!";
        }
    }

    @Override
    public List<OrderDTO> getAll() {
        List<Order> orders = orderRepository.findAll();
        List<OrderDTO> orderDTOS = new ArrayList<>();
        for (Order order:orders){
            orderDTOS.add(modelMapper.map(order, OrderDTO.class));
        }
        return orderDTOS;
    }
}
