package com.FlashCart.FlashSaleSystem.Models;

import jakarta.persistence.*;
import org.modelmapper.internal.bytebuddy.asm.Advice;

import java.time.LocalDateTime;

@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    private Double totalPrice;
    private String status;
    private Integer quantity;
    private String idempotencyKey;
    private String paymentStatus;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "flashSale_id")
    private FlashSale flashSale;

    public Order() {
    }

    public Order(Long orderId, Double totalPrice, String status, User user, FlashSale flashSale) {
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.status = status;
        this.user = user;
        this.flashSale = flashSale;
    }

    public Order(Long orderId, Double totalPrice, String status, Integer quantity, String idempotencyKey, User user, FlashSale flashSale) {
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.status = status;
        this.quantity = quantity;
        this.idempotencyKey = idempotencyKey;
        this.user = user;
        this.flashSale = flashSale;
    }

    public Order(Long orderId, Double totalPrice, String status, Integer quantity, String idempotencyKey, String paymentStatus, User user, LocalDateTime createdAt, FlashSale flashSale) {
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.status = status;
        this.quantity = quantity;
        this.idempotencyKey = idempotencyKey;
        this.paymentStatus = paymentStatus;
        this.user = user;
        this.createdAt = createdAt;
        this.flashSale = flashSale;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public FlashSale getFlashSale() {
        return flashSale;
    }

    public void setFlashSale(FlashSale flashSale) {
        this.flashSale = flashSale;
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
