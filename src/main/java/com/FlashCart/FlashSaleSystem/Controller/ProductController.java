package com.FlashCart.FlashSaleSystem.Controller;

import com.FlashCart.FlashSaleSystem.DTOs.ProductDTO;
import com.FlashCart.FlashSaleSystem.Service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    //resolving all the dependency using dependency injection
    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    //method to get all the products
    @GetMapping
    public ResponseEntity<List<ProductDTO>> allProducts(){
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }
    //method to get product by id
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> ProductById(@PathVariable Long productId){
        return new ResponseEntity<>(productService.geProductById(productId), HttpStatus.OK);
    }
}
