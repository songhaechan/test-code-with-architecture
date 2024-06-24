package com.example.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

import com.example.demo.exception.CertificationCodeNotMatchedException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.UserStatus;
import com.example.demo.model.dto.UserCreateDto;
import com.example.demo.model.dto.UserUpdateDto;
import com.example.demo.repository.UserEntity;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;

@SpringBootTest
@SqlGroup({
    @Sql(value="/sql/import_service.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
    @Sql(value = "/sql/import_service_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
})
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @MockBean
    private JavaMailSender javaMailSender;

    @Test
    void getByEmail_이_active_유저만_가져올수있다() {
        // given
        String email =  "bukak2019@naver.com";
        // when
        UserEntity result = userService.getByEmail(email);
        // then
        assertThat(result.getStatus()).isSameAs(UserStatus.ACTIVE);
    }

    @Test
    void PENDING인_유저는_찾아올수없다() {
        // given
        String email = "bukak2020@naver.com";
        // when
        // then
        assertThatThrownBy(()->{
            userService.getByEmail(email);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getbyId는_pending_상태인_유저는_찾아올수없다() {
        // given
        // when
        // then
        assertThatThrownBy(()->{
            userService.getById(2L);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void userCreateDto를_이용해_유저를_생성할수있다() {
        // given
        UserCreateDto userCreateDto = UserCreateDto.builder()
            .email("bukak20@naver.com")
            .address("Gyongi")
            .nickname("kek")
            .build();
        BDDMockito.doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));
        // when
        UserEntity userEntity = userService.create(userCreateDto);
        // then
        assertThat(userEntity.getId()).isNotNull();
        assertThat(userEntity.getStatus()).isEqualTo(UserStatus.PENDING);
    }

    @Test
    void userCreateDto를_이용해_유저를_tnwjd할수있다() {
        // given
        UserUpdateDto userUpdateDto = UserUpdateDto.builder()
            .address("kao")
            .nickname("kek")
            .build();
        BDDMockito.doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));
        // when
        userService.update(1,userUpdateDto);
        // then
        UserEntity userEntity = userService.getById(1L);
        assertThat(userEntity.getId()).isNotNull();
        assertThat(userEntity.getAddress()).isEqualTo("kao");
        assertThat(userEntity.getNickname()).isEqualTo("kek");
    }

    @Test
    void login() {
        // given
        userService.login(1);
        // when

        // then
        UserEntity userEntity = userService.getById(1);
        assertThat(userEntity.getLastLoginAt()).isGreaterThan(0); // FIXME
    }

    @Test
    void PENDING상태의_사용자는_인증코드로_활성화시킬수있다() {
        // given
        // when
        userService.verifyEmail(2L,"aaaaa-aaaaaa-aaaaa-aaaaaaa");
        UserEntity userEntity = userService.getById(2L);
        // then
        assertThat(userEntity.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void PENDING상태의_사용자는_인증이_실패할수있다() {
        // given
        // when
        // then
        assertThatThrownBy(()->{
            userService.verifyEmail(2L,"aaaa-aaaaaa-aaaaa-aaaaaaa");
        }).isInstanceOf(CertificationCodeNotMatchedException.class);
    }


}
