package com.app.fooddelivery.service;

import com.app.fooddelivery.entity.FoodOrder;
import com.app.fooddelivery.exception.ResourceNotFoundException;
import com.app.fooddelivery.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public FoodOrder saveOrder(FoodOrder order) {
        return orderRepository.save(order);
    }

    public List<FoodOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    public FoodOrder getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
    }

    public List<FoodOrder> getOrdersByUserId(Long userId) {
        return orderRepository.findByUser_Id(userId);
    }

    public List<FoodOrder> getOrdersByAssignedAdminId(Long adminId) {
        return orderRepository.findByAssignedAdminId(adminId);
    }
}
