package org.example.service;

import org.assertj.core.api.Assertions;
import org.example.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserJdbcRepositoryTest {

    UserJdbcRepository repository;
    @Test
    @DisplayName("유저 아이디를 입력해서 유저를 반환을 받는다.")
    public void findByUserIdTest() {
        String userId = "bsa0530";

        repository = new UserJdbcRepository();
        User byId = repository.findById(userId);
        Assertions.assertThat(userId).isEqualTo(byId.getUserId());
    }

    @Test
    @DisplayName("(중복유저는 안됌)유저를 만든다.")
    public void saveUserTest(){
        String userId = "test1";
        String userPw = "test1";

        repository = new UserJdbcRepository();
        repository.save(userId, userPw);

        User byId = repository.findById(userId);
        Assertions.assertThat(userId).isEqualTo(byId.getUserId());

    }
}