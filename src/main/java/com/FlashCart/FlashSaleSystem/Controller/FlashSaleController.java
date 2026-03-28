package com.FlashCart.FlashSaleSystem.Controller;

import com.FlashCart.FlashSaleSystem.DTOs.FlashSaleDTO;
import com.FlashCart.FlashSaleSystem.Service.FlashSaleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    //method to get the sale using id
    @GetMapping("/{saleId}")
    public ResponseEntity<FlashSaleDTO> getSaleById(@PathVariable Long saleId){
        return new ResponseEntity<>(flashSaleService.getSaleById(saleId),HttpStatus.OK);
    }
}
