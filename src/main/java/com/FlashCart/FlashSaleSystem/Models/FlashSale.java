package com.FlashCart.FlashSaleSystem.Models;

import jakarta.persistence.*;

import java.sql.Time;
import java.time.LocalDateTime;

@Entity
public class FlashSale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long saleId;
    private LocalDateTime startAt;
    private LocalDateTime endsAt;
    private Integer saleStock;
    private Double specialPrice;

    @OneToOne
    @JoinColumn(name = "Product_Id")
    private Product product;

    public FlashSale() {
    }

    public FlashSale(Long saleId, LocalDateTime startAt, Integer saleStock, LocalDateTime endsAt ,Double specialPrice) {
        this.saleId = saleId;
        this.startAt = startAt;
        this.saleStock = saleStock;
        this.endsAt = endsAt;
        this.specialPrice=specialPrice;
    }

    public FlashSale(Long saleId, LocalDateTime startAt, LocalDateTime endsAt, Integer saleStock, Product product,Double specialPrice) {
        this.saleId = saleId;
        this.startAt = startAt;
        this.endsAt = endsAt;
        this.saleStock = saleStock;
        this.product = product;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Double getSpecialPrice() {
        return specialPrice;
    }

    public void setSpecialPrice(Double specialPrice) {
        this.specialPrice = specialPrice;
    }
}
