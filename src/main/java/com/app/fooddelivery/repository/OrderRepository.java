package com.app.fooddelivery.repository;

import com.app.fooddelivery.entity.FoodOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<FoodOrder, Long> {
    List<FoodOrder> findByUser_Id(Long userId);

    List<FoodOrder> findByAssignedAdminId(Long assignedAdminId);
}
