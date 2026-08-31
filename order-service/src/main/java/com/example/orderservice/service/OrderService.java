package com.example.orderservice.service;

import com.example.orderservice.client.UserClient;
import com.example.orderservice.dto.CreateOrderRequest;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderItem;
import com.example.orderservice.repository.OrderRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserClient userClient;

    public OrderService(
            OrderRepository orderRepository,
            UserClient userClient) {

        this.orderRepository = orderRepository;
        this.userClient = userClient;
    }

    @Transactional
    public Order createOrder(CreateOrderRequest request) {

        // Verify user exists
        userClient.getUserById(request.getUserId());

        // Create Order
        Order order = new Order();

        order.setUserId(request.getUserId());

        // Create Order Items
        for (var itemRequest : request.getItems()) {

            OrderItem orderItem = new OrderItem();

            orderItem.setProductId(itemRequest.getProductId());
            orderItem.setQuantity(itemRequest.getQuantity());

            orderItem.setOrder(order);

            order.getItems().add(orderItem);
        }

        return orderRepository.save(order);
    }

    public Order getOrderById(Long orderId) {

        return orderRepository.findById(orderId)
                .orElse(null);
    }
}