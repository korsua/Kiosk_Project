package org.example.repository;

import org.example.model.Order;

import java.util.List;

public interface OrderRepository {
    void close();
    long save(String userId,long totalPrice);
    Order updateByOrderId(Long orderId, String status);
    Order findAllByUserId(String userId);
    List<Order> findAll();

    int updateStatusByOrderId(long orderId);
}
