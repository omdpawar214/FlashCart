package com.FlashCart.FlashSaleSystem.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class ProductDTO {
    private Long productId;
    private String name;
    private String description;
    private Double price;
    private Integer stock;

    public ProductDTO() {
    }

    public ProductDTO(Long productId, String name, String description, Integer stock, Double price) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.stock = stock;
        this.price = price;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
