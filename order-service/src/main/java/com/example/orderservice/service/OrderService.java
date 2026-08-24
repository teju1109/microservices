package com.example.orderservice.service;

import com.example.orderservice.model.Order;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OrderService {

    private final Map<Long, Order> orders = new HashMap<>();

    public OrderService() {
        orders.put(5001L, new Order(5001L, 1001L));
        orders.put(5002L, new Order(5002L, 1002L));
        orders.put(5003L, new Order(5003L, 1003L));
    }

    public Order getOrderById(Long orderId) {
        return orders.get(orderId);
    }
}