package com.app.fooddelivery.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodResponse {
    private Long id;
    private String name;
    private String description;
    private double price;
    private String imageUrl;
}
