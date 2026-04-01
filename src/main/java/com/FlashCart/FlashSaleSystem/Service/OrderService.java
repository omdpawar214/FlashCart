package com.FlashCart.FlashSaleSystem.Service;

import com.FlashCart.FlashSaleSystem.DTOs.OrderDTO;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {
    OrderDTO purchase(Long saleId, Long userId, Integer quantity);

    String pay(Long orderId, Boolean paymentSuccess);
}
