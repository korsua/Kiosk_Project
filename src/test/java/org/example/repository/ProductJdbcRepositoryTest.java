package org.example.repository;

import org.assertj.core.api.Assertions;
import org.example.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

class ProductJdbcRepositoryTest {
    ProductJdbcRepository repository = new ProductJdbcRepository();
    @ParameterizedTest
    @ValueSource(strings = {"라면","볶음밥","콜라","우유"})
    public void save(String name){
        int count = repository.save(name, 1500L);
        Assertions.assertThat(count).isEqualTo(1);
    }

    @Test
    public void 제품이름_검색(){
        Product 라면 = repository.findByName("라면");

        Assertions.assertThat("라면").isEqualTo(라면.getName());
    }
    @Test
    public void 제품_전부_검색(){
        List<Product> all =repository.findAll();

        Assertions.assertThat(all.size()).isEqualTo(4);
    }
    @Test
    public void 제품_삭제(){
        String name = "라면";
        int count = repository.delete(name);
        Assertions.assertThat(count).isEqualTo(1);
    }

}