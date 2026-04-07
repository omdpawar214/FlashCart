package com.FlashCart.FlashSaleSystem.Service;

import com.FlashCart.FlashSaleSystem.Enums.OrderStatus;
import com.FlashCart.FlashSaleSystem.Enums.PaymentStatus;
import com.FlashCart.FlashSaleSystem.Models.Order;
import com.FlashCart.FlashSaleSystem.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class OrderExpiryService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RedisService redisService;

    @Scheduled(fixedRate = 60000) // every 1 min
    public void handleExpiredOrders() {

        List<Order> pendingOrders = orderRepository.findAll();

        for (Order order : pendingOrders) {

            if (!order.getPaymentStatus().equals(PaymentStatus.PENDING.name())) {
                continue;
            }

            String expiryKey = "order:expiry:" + order.getOrderId();

            Boolean exists = redisTemplate.hasKey(expiryKey);

            // if key DOES NOT exist → expired
            if (Boolean.FALSE.equals(exists)) {

                order.setStatus(OrderStatus.CANCELLED.name());
                order.setPaymentStatus(PaymentStatus.FAILED.name());

                String stockKey = "FlashSale:Stock" + order.getFlashSale().getSaleId();
                redisService.increaseStock(stockKey, order.getQuantity());

                orderRepository.save(order);
            }
        }
    }
}