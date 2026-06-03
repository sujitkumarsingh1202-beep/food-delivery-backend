package com.app.fooddelivery.service;

import com.app.fooddelivery.dto.request.PaymentRequest;
import com.app.fooddelivery.dto.response.PaymentResponse;
import com.app.fooddelivery.entity.Payment;
import com.app.fooddelivery.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public PaymentResponse processPayment(PaymentRequest request) {
        Payment payment = new Payment();
        payment.setOrderId(request.getOrderId());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setAmount(request.getAmount());
        
        // Mock processing logic
        String status = "FAILED";
        String transactionId = null;
        if ("COD".equalsIgnoreCase(request.getPaymentMethod())) {
            status = "SUCCESS";
        } else if ("UPI".equalsIgnoreCase(request.getPaymentMethod()) || "CARD".equalsIgnoreCase(request.getPaymentMethod())) {
            status = "SUCCESS";
            transactionId = UUID.randomUUID().toString();
        }

        payment.setStatus(status);
        payment.setTransactionId(transactionId);
        Payment saved = paymentRepository.save(payment);
        return new PaymentResponse(saved.getId(), saved.getOrderId(), saved.getPaymentMethod(), saved.getAmount(), saved.getStatus(), saved.getTransactionId());
    }

    public Payment savePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public Optional<Payment> getPaymentByOrderId(Long orderId) {
        return paymentRepository.findByOrder_Id(orderId);
    }
}
