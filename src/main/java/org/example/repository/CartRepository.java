package org.example.repository;

import org.example.model.Cart;
import org.example.model.Product;

import java.util.List;

public interface CartRepository {
    void save(Product product, String userId);
    Cart findById(Long cartId);
    int updateById(Long cartId, int count);
    List<Cart> findAll();
    int deleteAll();


}
