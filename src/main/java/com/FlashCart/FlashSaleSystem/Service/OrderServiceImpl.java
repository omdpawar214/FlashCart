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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    @Override
    @Transactional
    public OrderDTO purchase(Long saleId, Long userId, Integer quantity) {
        //checking if user exists
        User currUser = userRepository.findById(userId).orElseThrow(()->
                new ResourceNotFoundException("User","userId",userId));
        //check for Idempotency key constraints
        String key = saleId+"_"+userId;
        Order existingOrder = orderRepository.findByIdempotencyKey(key);
        if (existingOrder!=null) throw new APIException("Current User have already ordered From this sale");
        //check for Active flash sale

        FlashSale sale = flashSaleRepository.findById(saleId).orElseThrow(()->
                new ResourceNotFoundException("flashSale","FlashSaleId",saleId));
        if (sale.getStartAt().isAfter(LocalDateTime.now())||sale.getEndsAt().isBefore(LocalDateTime.now())){
            throw new APIException("Sale with id-"+saleId+" is InActive");
        }
        //check for the quantity
        if (sale.getSaleStock()<quantity) throw new APIException("Out of Stock!!");
        //create the order
        Order currentOrder = new Order();
        currentOrder.setIdempotencyKey(key);
        currentOrder.setCreatedAt(LocalDateTime.now());
        currentOrder.setPaymentStatus(String.valueOf(PaymentStatus.PENDING));
        currentOrder.setStatus(String.valueOf(OrderStatus.CREATED));
        currentOrder.setUser(currUser);
        currentOrder.setFlashSale(sale);

        sale.setSaleStock(sale.getSaleStock()-quantity);
        currentOrder.setQuantity(quantity);
        currentOrder.setTotalPrice(sale.getSpecialPrice()*quantity);
        //save the order
        flashSaleRepository.save(sale);
        Order savedOrder = orderRepository.save(currentOrder);
        //return the DTO
        return modelMapper.map(savedOrder, OrderDTO.class);
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
        //if payment is success update the payment and order status in order
        if(paymentSuccess){
            currOrder.setPaymentStatus(String.valueOf(PaymentStatus.SUCCESS));
            currOrder.setStatus(String.valueOf(OrderStatus.SUCCESS));
            orderRepository.save(currOrder);
            return "Payment Success";
        }
        //else update the payment and order status and return the stock to the sale
        else {
            currOrder.setPaymentStatus(String.valueOf(PaymentStatus.FAILED));
            currOrder.setStatus(String.valueOf(OrderStatus.CANCELLED));
            sale.setSaleStock(sale.getSaleStock()+currOrder.getQuantity());
            //saving the updated models
            flashSaleRepository.save(sale);
            orderRepository.save(currOrder);

            return "Order cancelled!";
        }
    }
}
