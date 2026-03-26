package com.FlashCart.FlashSaleSystem.Controller;

import com.FlashCart.FlashSaleSystem.DTOs.FlashSaleDTO;
import com.FlashCart.FlashSaleSystem.Service.FlashSaleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/flashSale")
public class FlashSaleController {
    private final FlashSaleService flashSaleService;

    public FlashSaleController(FlashSaleService flashSaleService) {
        this.flashSaleService = flashSaleService;
    }

    //method to create Flash sale
    @PostMapping
    public ResponseEntity<FlashSaleDTO> createSale(@RequestBody FlashSaleDTO flashSaleDTO){
        return new ResponseEntity<>(flashSaleService.createSale(flashSaleDTO), HttpStatus.CREATED);
    }
}
