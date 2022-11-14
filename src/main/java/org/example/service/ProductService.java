package org.example.service;

import org.example.model.Product;
import org.example.repository.ProductJdbcRepository;
import org.example.repository.ProductRepository;

import java.sql.SQLException;
import java.util.List;

public class ProductService {
    private static ProductService instance = null;
    ProductRepository repository = null;

    private ProductService(ProductRepository repository) {
        this.repository = repository;
    }
    public static ProductService getInstance(){
        if(instance == null){
            instance = new ProductService(new ProductJdbcRepository());
        }
        return instance;
    }
    public List<Product> findProducts(){
        return repository.findAll();
    }
    public Product findProductByName(String name){
        return repository.findByName(name);
    }
    public Product findProductById(Long id){
        return repository.findById(id);
    }
    public void saveProduct(Product product) throws SQLException, ClassNotFoundException {
        repository.save(product.getName(), product.getPrice());
    }

}
