package org.example.service;

import org.example.model.Product;

public interface ProductRepository {
    Product save();
    Product findByName();
}
