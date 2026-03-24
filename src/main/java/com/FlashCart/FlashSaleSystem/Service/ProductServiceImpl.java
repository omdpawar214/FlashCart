package com.FlashCart.FlashSaleSystem.Service;

import com.FlashCart.FlashSaleSystem.DTOs.ProductDTO;
import com.FlashCart.FlashSaleSystem.Models.Product;
import com.FlashCart.FlashSaleSystem.Repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{
    private ProductRepository productRepository;
   private ModelMapper modelMapper;
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        //fetching all the products from repository
        List<Product> products = productRepository.findAll();
        //converting products to productDTO
        List<ProductDTO> productDTOS = new ArrayList<>();
        for (Product product:products){
            productDTOS.add(modelMapper.map(product, ProductDTO.class));
        }
        //returning the result
        return productDTOS;
    }

    @Override
    public ProductDTO geProductById(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(()->
                new RuntimeException("Product Does not exist with ProductId: "+productId));
        return modelMapper.map(product, ProductDTO.class);
    }
}
