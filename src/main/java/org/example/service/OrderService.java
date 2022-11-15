package org.example.service;

import org.example.repository.OrderJdbcRepository;
import org.example.repository.OrderRepository;

public class OrderService {
    OrderRepository orderRepository;
    private static OrderService instance;


    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public static OrderService getInstance() {
        if(instance == null) instance = new OrderService(new OrderJdbcRepository());
        return instance;
    }

    public long save(String userId, long totalPrice){
        return orderRepository.save(userId,totalPrice);
    }
}
