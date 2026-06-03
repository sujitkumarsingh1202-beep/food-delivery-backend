package com.app.fooddelivery.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationResponse {
    private Long id;
    private Long userId;
    private Double latitude;
    private Double longitude;
    private String address;
}
