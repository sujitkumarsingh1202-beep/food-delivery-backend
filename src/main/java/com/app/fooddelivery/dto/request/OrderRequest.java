package com.app.fooddelivery.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    @NotBlank(message = "Order items are required")
    private String items;

    @NotNull(message = "Total amount is required")
    @Min(value = 0, message = "Total amount must be zero or greater")
    private Double totalAmount;

    @NotBlank(message = "Delivery address is required")
    private String deliveryAddress;

    @NotNull(message = "Location ID is required")
    private Long locationId;
}
