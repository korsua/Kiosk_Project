package org.example.repository;

import org.example.model.Cart;
import org.example.model.OrderDetail;

import java.util.List;

public interface OrderDetailRepository {
    void save(long orderId, Cart cart);

    List<OrderDetail> findAllbyOrderId(long orderId);
}
