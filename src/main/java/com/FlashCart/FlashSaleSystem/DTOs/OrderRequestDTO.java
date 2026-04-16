package com.FlashCart.FlashSaleSystem.DTOs;

public class OrderRequestDTO {

    private Long saleId;
    private Long userId;
    private Integer quantity;

    public OrderRequestDTO() {
    }

    public OrderRequestDTO(Long saleId, Integer quantity, Long userId) {
        this.saleId = saleId;
        this.quantity = quantity;
        this.userId = userId;
    }

    public Long getSaleId() {
        return saleId;
    }

    public void setSaleId(Long saleId) {
        this.saleId = saleId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
