package org.example.repository;

import org.assertj.core.api.Assertions;
import org.example.model.Cart;
import org.example.model.Product;
import org.example.service.ProductService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CartJdbcRepositoryTest {

    CartRepository repository = new CartJdbcRepository();



    @Test
    void save() {
        ProductService productService = mock(ProductService.class);
        when(productService.findProductByName("음료수")).thenReturn(new Product(1L,"음료수",1000L));
        Product product = productService.findProductByName("음료수");

        repository.save(product,"test");
    }

    @Test
    void findById() {
        Long cartId = 1L;
        Cart byId = repository.findById(cartId);
        Assertions.assertThat(byId.getCartId()).isEqualTo(cartId);

    }

    @Test
    void updateById() {
    }

    @Test
    void findAll() {
    }

    @Test
    void deleteAll() {
    }
}