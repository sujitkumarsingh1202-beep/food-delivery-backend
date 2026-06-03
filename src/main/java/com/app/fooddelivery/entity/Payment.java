package com.app.fooddelivery.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private FoodOrder order;

    private String paymentMethod;
    private double amount;
    private String status;
    private String transactionId;

    public Long getOrderId() {
        return order != null ? order.getId() : null;
    }

    public void setOrderId(Long orderId) {
        if (this.order == null) {
            this.order = new FoodOrder();
        }
        this.order.setId(orderId);
    }
}
