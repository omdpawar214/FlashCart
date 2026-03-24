package com.FlashCart.FlashSaleSystem.Controller;

import com.FlashCart.FlashSaleSystem.DTOs.ProductDTO;
import com.FlashCart.FlashSaleSystem.Service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    //method to create the product
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO){
        return new ResponseEntity<>(productService.createProduct(productDTO),HttpStatus.CREATED);
    }

    //method to create the product
    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId){
        return new ResponseEntity<>(productService.deleteProduct(productId),HttpStatus.OK);
    }

    //method to update the product
    @PutMapping
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductDTO productDTO){
        return new ResponseEntity<>(productService.updateProduct(productDTO),HttpStatus.ACCEPTED);
    }
}
