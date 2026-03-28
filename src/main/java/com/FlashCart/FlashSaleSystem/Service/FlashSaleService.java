package com.FlashCart.FlashSaleSystem.Service;

import com.FlashCart.FlashSaleSystem.DTOs.FlashSaleDTO;

public interface FlashSaleService {
    FlashSaleDTO createSale(FlashSaleDTO flashSaleDTO);

    FlashSaleDTO getSaleById(Long saleId);
}
