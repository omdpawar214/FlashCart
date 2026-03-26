package com.FlashCart.FlashSaleSystem.Repository;

import com.FlashCart.FlashSaleSystem.Models.FlashSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlashSaleRepository extends JpaRepository<FlashSale ,Long> {
}
