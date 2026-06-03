package com.app.fooddelivery.repository;

import com.app.fooddelivery.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<Food, Long> {
}
