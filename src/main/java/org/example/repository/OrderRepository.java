package org.example.repository;

import org.example.model.Order;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository {
    void close();
    long save(String userId,long totalPrice,String message);
    Order updateByOrderId(Long orderId, String status);
    Order findAllByUserId(String userId);
    List<Order> findAll();

    int updateStatusByOrderId(long orderId);

    String[][] findAllProductOrder(LocalDate start, LocalDate end, int index);
}
