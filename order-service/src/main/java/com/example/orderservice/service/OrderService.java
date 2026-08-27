package com.example.orderservice.service;

import com.example.orderservice.dto.CreateOrderRequest;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderItem;
import com.example.orderservice.repository.OrderRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order createOrder(CreateOrderRequest request) {

        // Create Order
        Order order = new Order();

        // Set user ID
        order.setUserId(request.getUserId());

        // Create Order Items
        for (var itemRequest : request.getItems()) {

            OrderItem orderItem = new OrderItem();

            orderItem.setProductId(itemRequest.getProductId());
            orderItem.setQuantity(itemRequest.getQuantity());

            // IMPORTANT:
            // Connect OrderItem to its parent Order
            orderItem.setOrder(order);

            order.getItems().add(orderItem);
        }

        // Save Order
        // Order ID will be generated automatically by MySQL
        // OrderItems will also be saved because of CascadeType.ALL
        return orderRepository.save(order);
    }

    public Order getOrderById(Long orderId) {

        return orderRepository.findById(orderId)
                .orElse(null);
    }
}