package org.example.repository;

import org.assertj.core.api.Assertions;
import org.example.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

class UserJdbcRepositoryTest {

    UserJdbcRepository repository;
    @Test
    @DisplayName("유저 아이디를 입력해서 유저를 반환을 받는다.")
    public void findByUserIdTest() throws SQLException {
        String userId = "bsa0530";

        repository = new UserJdbcRepository();
        User byId = repository.findById(userId);
        Assertions.assertThat(userId).isEqualTo(byId.getUserId());
    }

    @Test
    @DisplayName("(중복유저는 안됌)유저를 만든다.")
    public void saveUserTest(){
        String userId = "test";
        String userPw = "test1";

        repository = new UserJdbcRepository();
        Assertions.assertThatThrownBy(() ->{
            repository.save(userId,userPw);
        }).isInstanceOf(RuntimeException.class);
    }
}