package org.example.repository;

import org.example.model.Cart;

public interface OrderDetailRepository {
    void save(long orderId, Cart cart);
}
