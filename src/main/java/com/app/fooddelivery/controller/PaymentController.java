package com.app.fooddelivery.controller;

import com.app.fooddelivery.dto.request.PaymentRequest;
import com.app.fooddelivery.dto.response.PaymentResponse;
import com.app.fooddelivery.entity.FoodOrder;
import com.app.fooddelivery.service.OrderService;
import com.app.fooddelivery.service.PaymentService;
import com.app.fooddelivery.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;
    private final OrderService orderService;
    private final UserService userService;

    public PaymentController(PaymentService paymentService, OrderService orderService, UserService userService) {
        this.paymentService = paymentService;
        this.orderService = orderService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> processPayment(@Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(paymentService.processPayment(request));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<PaymentResponse> getPaymentStatus(@PathVariable Long orderId, Authentication authentication) {
        String username = authentication.getName();
        Long authenticatedUserId = userService.getUserByUsername(username).getId();

        FoodOrder order = orderService.getOrderById(orderId);
        if (order == null || !order.getUserId().equals(authenticatedUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return paymentService.getPaymentByOrderId(orderId)
                .map(payment -> ResponseEntity.ok(new PaymentResponse(payment.getId(), payment.getOrderId(), payment.getPaymentMethod(), payment.getAmount(), payment.getStatus(), payment.getTransactionId())))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
