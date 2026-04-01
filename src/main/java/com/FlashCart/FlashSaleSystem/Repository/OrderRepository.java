package com.FlashCart.FlashSaleSystem.Repository;

import com.FlashCart.FlashSaleSystem.Models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    Order findByIdempotencyKey(String key);
}
