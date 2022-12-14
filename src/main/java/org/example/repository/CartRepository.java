package org.example.repository;

import org.example.model.Cart;
import org.example.model.Product;

import java.sql.SQLException;
import java.util.List;

public interface CartRepository {
    void close();
    void save(Product product, String userId) throws SQLException;
    Cart findById(Long cartId);
    int updateById(Cart cart, long count);

    List<Cart> findAllByUserId(String userId);

    int deleteAllByUserId(String userId);

    Cart existCart(Long productId,String userId);
    int deleteByCartId(long cartId);
}
