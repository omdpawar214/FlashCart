package com.FlashCart.FlashSaleSystem.Service;

import com.FlashCart.FlashSaleSystem.DTOs.OrderDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    String purchase(Long saleId, Long userId, Integer quantity);

    String pay(Long orderId, Boolean paymentSuccess);

    List<OrderDTO> getAll();
}
