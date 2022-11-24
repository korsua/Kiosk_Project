package org.example.service;

import org.example.model.Order;
import org.example.repository.OrderJdbcRepository;
import org.example.repository.OrderRepository;

import java.util.List;

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
    public List<Order> findOrders(){
        return orderRepository.findAll();
    }
    public int processOrderByOrderId(long orderId){
        return orderRepository.updateStatusByOrderId(orderId);
    }
}
