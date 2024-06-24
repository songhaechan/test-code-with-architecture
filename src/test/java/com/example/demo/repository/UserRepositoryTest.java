package com.example.demo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.model.UserStatus;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql("/sql/import.sql")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void 유저_데이터를_찾아올수있다() {
        // given
        // when
        Optional<UserEntity> result = userRepository.findByIdAndStatus(1L,
            UserStatus.ACTIVE);
        // then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    void 데이터가_없으면_optional_empty를_내려준다() {
        // given
        // when
        Optional<UserEntity> result = userRepository.findByIdAndStatus(1L,
            UserStatus.PENDING);
        // then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    void 이메일로_유저_데이터를_찾아올수있다() {
        // given
        // when
        Optional<UserEntity> result = userRepository.findByEmailAndStatus("bukak2019@naver.com",
            UserStatus.ACTIVE);
        // then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    void 이메일로_찾은_데이터가_없으면_optional_empty를_내려준다() {
        // given
        // when
        Optional<UserEntity> result = userRepository.findByEmailAndStatus("bukak2019@naver.com",
            UserStatus.PENDING);
        // then
        assertThat(result.isEmpty()).isTrue();
    }
}
