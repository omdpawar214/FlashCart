package com.FlashCart.FlashSaleSystem.Service;

import com.FlashCart.FlashSaleSystem.Enums.OrderStatus;
import com.FlashCart.FlashSaleSystem.Enums.PaymentStatus;
import com.FlashCart.FlashSaleSystem.Models.Order;
import com.FlashCart.FlashSaleSystem.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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

        // scan all expiry keys
        Set<String> keys = redisTemplate.keys("order:expiry:*");

        for (String key : keys) {
            Long ttl = redisTemplate.getExpire(key);

            // if expired (TTL <= 0)
            if (ttl != null && ttl <= 0) {

                String orderIdStr = key.split(":")[2];
                Long orderId = Long.parseLong(orderIdStr);

                Order order = orderRepository.findById(orderId).orElse(null);

                if (order != null && order.getPaymentStatus().equals("PENDING")) {

                    // cancel order
                    order.setStatus(String.valueOf(OrderStatus.CANCELLED));
                    order.setPaymentStatus(String.valueOf(PaymentStatus.FAILED));

                    // return stock
                    String stockKey = "flashSale:stock" + order.getFlashSale().getSaleId();
                    redisService.increaseStock(stockKey, order.getQuantity());

                    orderRepository.save(order);
                }

                redisTemplate.delete(key);
            }
        }
    }
}