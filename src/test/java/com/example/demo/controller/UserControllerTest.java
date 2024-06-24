package com.example.demo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.model.UserStatus;
import com.example.demo.model.dto.UserUpdateDto;
import com.example.demo.repository.UserEntity;
import com.example.demo.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SqlGroup({
    @Sql(value="/sql/import_controller.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
    @Sql(value = "/sql/import_controller_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
})
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 사용자는_특정_유저의_정보를_전달_받을수있다() throws Exception {
        mockMvc.perform(get("/api/users/1 "))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void 사용자는_존재하지않는_유저의_아이디로_요청하면_404응답을_받는다() throws Exception {
        mockMvc.perform(get("/api/users/1231 "))
            .andExpect(status().isNotFound())
            .andExpect(content().string("Users에서 ID 1231를 찾을 수 없습니다."));
    }

    @Test
    void 사용자는_인증코드로_계정을_활성화할수있다() throws Exception {
        mockMvc.perform(get("/api/users/2/verify")
                .queryParam("certificationCode","aaaaa-aaaaaa-aaaaa-aaaaaaa"))
            .andExpect(status().isFound());
        UserEntity userEntity = userRepository.findById(2L).get();
        Assertions.assertThat(userEntity.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void 사용자는_정보를_불러올때_주소도_불러올수있다() throws Exception {
        mockMvc.perform(get("/api/users/me")
                .header("EMAIL","bukak2019@naver.com"))
            .andExpect(status().isFound())
            .andExpect(jsonPath("$.address").value("Seoul"));
    }

    @Test
    void 일반사용자는_정보를_불러올때_주소를_불러올수없다() throws Exception {
        mockMvc.perform(get("/api/users/1")
                .header("EMAIL","bukak2019@naver.com"))
            .andExpect(status().isFound())
            .andExpect(jsonPath("$.address").doesNotExist());
    }

    @Test
    void 사용자는_정보를_수정할수있다() throws Exception {
        //given
        UserUpdateDto userUpdateDto = new UserUpdateDto("choi", "usa");
        mockMvc.perform(put("/api/users/me")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userUpdateDto)))
            .andExpect(status().isFound())
            .andExpect(jsonPath("$.nickname").value("choi"))
            .andExpect(jsonPath("$.address").value("usa"));
    }
}
