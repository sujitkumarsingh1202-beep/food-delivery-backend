package com.app.fooddelivery.controller;

import com.app.fooddelivery.dto.request.OrderRequest;
import com.app.fooddelivery.dto.response.OrderResponse;
import com.app.fooddelivery.entity.FoodOrder;
import com.app.fooddelivery.entity.Payment;
import com.app.fooddelivery.service.OrderService;
import com.app.fooddelivery.service.PaymentService;
import com.app.fooddelivery.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final PaymentService paymentService;
    private final UserService userService;

    public OrderController(OrderService orderService, PaymentService paymentService, UserService userService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponse>> listOrders() {
        List<OrderResponse> orders = orderService.getAllOrders().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/my")
    public ResponseEntity<List<OrderResponse>> getMyOrders(Authentication authentication) {
        String username = authentication.getName();
        Long userId = userService.getUserByUsername(username).getId();

        List<OrderResponse> orders = orderService.getOrdersByUserId(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(@Valid @RequestBody OrderRequest request, Authentication authentication) {
        String username = authentication.getName();
        Long authenticatedUserId = userService.getUserByUsername(username).getId();

        FoodOrder order = new FoodOrder();
        order.setUserId(authenticatedUserId);
        order.setItems(request.getItems());
        order.setDeliveryAddress(request.getDeliveryAddress());
        order.setTotalAmount(request.getTotalAmount());
        order.setStatus("PENDING");
        order.setLocationId(request.getLocationId());

            // Assign admin (default: admin id 1)
            order.setAssignedAdminId(1L);

        FoodOrder savedOrder = orderService.saveOrder(order);

        // Create payment record
        Payment payment = new Payment();
        payment.setOrderId(savedOrder.getId());
        payment.setAmount(savedOrder.getTotalAmount());
        payment.setStatus("PENDING");
        payment.setPaymentMethod("CARD"); // Default, can be updated later
        paymentService.savePayment(payment);

        return ResponseEntity.ok(mapToResponse(savedOrder));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable Long id, @RequestParam String status, Authentication authentication) {
        FoodOrder order = orderService.getOrderById(id);

        // Validation: assigned admin only
        if (order.getAssignedAdminId() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        String username = authentication.getName();
        Long currentAdminId = userService.getUserByUsername(username).getId();
        if (!order.getAssignedAdminId().equals(currentAdminId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        // Only assigned admin can update status
        order.setStatus(status);
        FoodOrder updatedOrder = orderService.saveOrder(order);
        return ResponseEntity.ok(mapToResponse(updatedOrder));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        Long authenticatedUserId = userService.getUserByUsername(username).getId();

        FoodOrder order = orderService.getOrderById(id);

        // Ownership Check
        if (!order.getUserId().equals(authenticatedUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only cancel your own orders");
        }

        // Status Check
        if (!"PENDING".equalsIgnoreCase(order.getStatus())) {
            return ResponseEntity.badRequest().body("Only PENDING orders can be cancelled. Current status: " + order.getStatus());
        }

        order.setStatus("CANCELLED");
        FoodOrder updatedOrder = orderService.saveOrder(order);
        return ResponseEntity.ok(mapToResponse(updatedOrder));
    }

    private OrderResponse mapToResponse(FoodOrder order) {
        return new OrderResponse(
            order.getId(),
            order.getUserId(),
            order.getItems(),
            order.getTotalAmount(),
            order.getDeliveryAddress(),
            order.getStatus(),
            order.getLocationId(),
            order.getCreatedAt(),
            order.getAssignedAdminId()
        );
    }
}
