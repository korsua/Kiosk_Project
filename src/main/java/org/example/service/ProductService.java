package org.example.service;

import org.example.model.Product;
import org.example.repository.ProductRepository;

import java.sql.SQLException;
import java.util.List;

public class ProductService {
    ProductRepository repository = null;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }
    public List<Product> findProducts(){
        return repository.findAll();
    }
    public Product findProductByName(String name){
        return repository.findByName(name);
    }
    public void saveProduct(Product product) throws SQLException, ClassNotFoundException {
        repository.save(product.getName(), product.getPrice());
    }

}
