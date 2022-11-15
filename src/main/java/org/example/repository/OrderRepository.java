package org.example.repository;

import org.example.model.Order;

public interface OrderRepository {
    long save(String userId,long totalPrice);
    Order updateByOrderId(Long orderId, String status);
    Order findAllByUserId(String userId);
}
