package org.example.repository;

import org.example.model.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductRepository {
    int save(String name,Long price) throws SQLException, ClassNotFoundException;
    Product findByName(String name);
    List<Product> findAll();
    int delete(String name);

    Product findById(Long id);
}
