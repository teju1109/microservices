package com.example.orderservice.controller;

import com.example.orderservice.client.UserClient;
import com.example.orderservice.dto.CreateOrderRequest;
import com.example.orderservice.dto.ErrorResponse;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.dto.UserResponse;
import com.example.orderservice.model.Order;
import com.example.orderservice.service.OrderService;

import jakarta.validation.Valid;

import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserClient userClient;
    private final CircuitBreakerFactory<?, ?> circuitBreakerFactory;

    public OrderController(
            OrderService orderService,
            UserClient userClient,
            CircuitBreakerFactory<?, ?> circuitBreakerFactory) {

        this.orderService = orderService;
        this.userClient = userClient;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(
            @Valid @RequestBody CreateOrderRequest request) {

        Order order = orderService.createOrder(request);

        return ResponseEntity.status(201).body(order);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrder(@PathVariable Long orderId) {

        Order order = orderService.getOrderById(orderId);

        if (order == null) {
            return ResponseEntity.status(404)
                    .body(new ErrorResponse(
                            "Order not found with id: " + orderId));
        }

        CircuitBreaker circuitBreaker =
                circuitBreakerFactory.create("userService");

        UserResponse user = circuitBreaker.run(
                () -> userClient.getUserById(order.getUserId()),
                throwable -> null
        );

        if (user == null) {
            return ResponseEntity.status(503)
                    .body(new ErrorResponse(
                            "User Service is currently unavailable"));
        }

        OrderResponse response = new OrderResponse(
                order.getOrderId(),
                order.getUserId(),
                user.getName(),
                user.getEmail()
        );

        return ResponseEntity.ok(response);
    }
}