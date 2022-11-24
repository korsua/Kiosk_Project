package org.example.repository;

import org.example.model.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductRepository {
    void close();
    int save(Product product) throws SQLException, ClassNotFoundException;
    Product findByName(String name);
    List<Product> findAll();
    int delete(String name);

    Product findById(Long id);

    List<Product> matcherProductsByName(String text);

    void updateProduct(Product product);
}
