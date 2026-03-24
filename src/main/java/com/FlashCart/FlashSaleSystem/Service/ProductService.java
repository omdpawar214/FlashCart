package com.FlashCart.FlashSaleSystem.Service;

import com.FlashCart.FlashSaleSystem.DTOs.ProductDTO;

import java.util.List;

public interface ProductService {
    List<ProductDTO> getAllProducts();

    ProductDTO createProduct(ProductDTO productDTO);

    String deleteProduct(Long productId);

    ProductDTO updateProduct(ProductDTO productDTO);

    ProductDTO geProductById(Long productId);
}
