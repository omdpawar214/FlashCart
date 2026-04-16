package com.FlashCart.FlashSaleSystem.Service;

import com.FlashCart.FlashSaleSystem.DTOs.OrderRequestDTO;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class KafkaConsumerService {
    @Autowired
    private FlashSaleRepository flashSaleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RedisService redisService;

    //method that will fetch the request from the topic in kafka
    @KafkaListener(topics = "flash-sale-orders", groupId = "flashcart-group")
    public void consume(OrderRequestDTO request){

        System.out.println("Processing order: " + request);

        //checking user and sale exist or not
        User currUser = userRepository.findById(request.getUserId()).orElseThrow(()->
                new ResourceNotFoundException("User","userId", request.getUserId()));

        FlashSale sale = flashSaleRepository.findById(request.getSaleId()).orElseThrow(()->
                new ResourceNotFoundException("flashSale","FlashSaleId", request.getSaleId()));

        String key = request.getSaleId()+"_"+request.getUserId();
        Order existingOrder = orderRepository.findByIdempotencyKey(key);
        if (existingOrder!=null) throw new APIException("Current User have already ordered From this sale");

        //creating order object
        Order currentOrder = new Order();
        currentOrder.setIdempotencyKey(key);
        currentOrder.setCreatedAt(LocalDateTime.now());
        currentOrder.setPaymentStatus(String.valueOf(PaymentStatus.PENDING));
        currentOrder.setStatus(String.valueOf(OrderStatus.CREATED));
        currentOrder.setUser(currUser);
        currentOrder.setFlashSale(sale);
        currentOrder.setQuantity(request.getQuantity());
        currentOrder.setTotalPrice(sale.getSpecialPrice() * request.getQuantity());
        //save the order
        Order savedOrder = orderRepository.save(currentOrder);
        //adding time to live for the order if the payment not done for order in stipulated time
        String expiryKey = "order:expiry:" + savedOrder.getOrderId();
        redisService.setWithTTL(expiryKey, String.valueOf(PaymentStatus.PENDING), 30);
    }
}
