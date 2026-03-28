package com.FlashCart.FlashSaleSystem.Controller;

import com.FlashCart.FlashSaleSystem.DTOs.FlashSaleDTO;
import com.FlashCart.FlashSaleSystem.Service.FlashSaleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    //method to get all the sales
    @GetMapping
    public ResponseEntity<List<FlashSaleDTO>> getAllSales(){
        return new ResponseEntity<>(flashSaleService.getAllSales(),HttpStatus.OK);
    }

    //method to get all the active sales
    @GetMapping("/active/")
    private ResponseEntity<List<FlashSaleDTO>> getAllActiveSales(){
        return new ResponseEntity<>(flashSaleService.getAllActiveSales(),HttpStatus.OK);

    }

    //method to update the Flash sale
    @PutMapping
    public ResponseEntity<FlashSaleDTO> updateSale(@RequestBody FlashSaleDTO flashSaleDTO) {
        return new ResponseEntity<>(flashSaleService.updateSale(flashSaleDTO), HttpStatus.ACCEPTED);
    }

    //method to delete sale
    @DeleteMapping("/{saleId}")
    public ResponseEntity<String> deleteSale(@PathVariable Long saleId) {
        return new ResponseEntity<>(flashSaleService.deleteSale(saleId), HttpStatus.OK);
    }
}
