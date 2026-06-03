package com.app.fooddelivery.controller;

import com.app.fooddelivery.dto.request.FoodRequest;
import com.app.fooddelivery.dto.response.FoodResponse;
import com.app.fooddelivery.entity.Food;
import com.app.fooddelivery.service.FoodService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/foods")
public class FoodController {

    private final FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @GetMapping
    public ResponseEntity<List<FoodResponse>> getAllFoods() {
        List<FoodResponse> foods = foodService.getAllFoods().stream()
                .map(food -> new FoodResponse(food.getId(), food.getName(), food.getDescription(), food.getPrice(), food.getImageUrl()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(foods);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FoodResponse> createFood(@Valid @RequestBody FoodRequest request) {
        Food food = new Food();
        food.setName(request.getName());
        food.setDescription(request.getDescription());
        food.setPrice(request.getPrice());
        food.setImageUrl(request.getImageUrl());
        Food savedFood = foodService.saveFood(food);
        return ResponseEntity.ok(new FoodResponse(savedFood.getId(), savedFood.getName(), savedFood.getDescription(), savedFood.getPrice(), savedFood.getImageUrl()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FoodResponse> updateFood(@PathVariable Long id, @Valid @RequestBody FoodRequest request) {
        Food food = foodService.getFoodById(id);
        food.setName(request.getName());
        food.setDescription(request.getDescription());
        food.setPrice(request.getPrice());
        food.setImageUrl(request.getImageUrl());
        Food updatedFood = foodService.saveFood(food);
        return ResponseEntity.ok(new FoodResponse(updatedFood.getId(), updatedFood.getName(), updatedFood.getDescription(), updatedFood.getPrice(), updatedFood.getImageUrl()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteFood(@PathVariable Long id) {
        foodService.deleteFood(id);
        return ResponseEntity.noContent().build();
    }
}
