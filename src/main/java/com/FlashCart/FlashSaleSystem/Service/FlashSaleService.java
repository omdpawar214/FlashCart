package com.FlashCart.FlashSaleSystem.Service;

import com.FlashCart.FlashSaleSystem.DTOs.FlashSaleDTO;

import java.util.List;

public interface FlashSaleService {
    FlashSaleDTO createSale(FlashSaleDTO flashSaleDTO);

    FlashSaleDTO getSaleById(Long saleId);

    List<FlashSaleDTO> getAllSales();

    List<FlashSaleDTO> getAllActiveSales();
}
