package com.app.fooddelivery.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private Long userId;
    private String items;
    private double totalAmount;
    private String deliveryAddress;
    private String status;
    private Long locationId;
    private LocalDateTime createdAt;
    private Long assignedAdminId;
}
