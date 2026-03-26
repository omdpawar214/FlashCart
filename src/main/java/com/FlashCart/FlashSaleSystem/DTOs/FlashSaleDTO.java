package com.FlashCart.FlashSaleSystem.DTOs;

import java.time.LocalDateTime;

public class FlashSaleDTO {
    private Long saleId;
    private LocalDateTime startAt;
    private LocalDateTime endsAt;
    private Integer saleStock;
    private Long productId;
    private Double specialPrice;

    public FlashSaleDTO() {
    }

    public FlashSaleDTO(Long saleId, LocalDateTime startAt, Integer saleStock, LocalDateTime endsAt,Long productId,Double specialPrice) {
        this.saleId = saleId;
        this.startAt = startAt;
        this.saleStock = saleStock;
        this.endsAt = endsAt;
        this.productId = productId;
        this.specialPrice=specialPrice;

    }

    public Integer getSaleStock() {
        return saleStock;
    }

    public void setSaleStock(Integer saleStock) {
        this.saleStock = saleStock;
    }

    public LocalDateTime getEndsAt() {
        return endsAt;
    }

    public void setEndsAt(LocalDateTime endsAt) {
        this.endsAt = endsAt;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public void setStartAt(LocalDateTime startAt) {
        this.startAt = startAt;
    }

    public Long getSaleId() {
        return saleId;
    }

    public void setSaleId(Long saleId) {
        this.saleId = saleId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Double getSpecialPrice() {
        return specialPrice;
    }

    public void setSpecialPrice(Double specialPrice) {
        this.specialPrice = specialPrice;
    }
}
