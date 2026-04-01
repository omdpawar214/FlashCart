package com.FlashCart.FlashSaleSystem.DTOs;

import java.time.LocalDateTime;

public class OrderDTO {
    private Long orderId;
    private Double totalPrice;
    private String status;
    private Integer quantity;
    private String idempotencyKey;
    private Long userId;
    private Long saleId;
    private String paymentStatus;
    private LocalDateTime createdAt;

    public OrderDTO() {
    }

    public OrderDTO(Long orderId, Double totalPrice, String status, Integer quantity, String idempotencyKey, Long userId, Long saleId) {
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.status = status;
        this.quantity = quantity;
        this.idempotencyKey = idempotencyKey;
        this.userId = userId;
        this.saleId = saleId;
    }

    public OrderDTO(Long orderId, Double totalPrice, String status, String idempotencyKey, Integer quantity, Long userId, String paymentStatus, Long saleId, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.status = status;
        this.idempotencyKey = idempotencyKey;
        this.quantity = quantity;
        this.userId = userId;
        this.paymentStatus = paymentStatus;
        this.saleId = saleId;
        this.createdAt = createdAt;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public void setIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSaleId() {
        return saleId;
    }

    public void setSaleId(Long saleId) {
        this.saleId = saleId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
