package com.example.demo.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.mock.FakeMailSender;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CertificationServiceTest {

    @Test
    void 이메일과_컨텐츠가_제대로_만들어져서_보내지는지_확인한다() {
        // given
        FakeMailSender fakeMailSender = new FakeMailSender();
        CertificationService certificationService = new CertificationService(fakeMailSender);
        // when
        certificationService.send("bukak2019@naver.com",1,"aaaaaaaa-aaaaaa-aaaaaa-aaaaaaaaa");

        // then
        assertThat(fakeMailSender.email).isEqualTo("bukak2019@naver.com");
        assertThat(fakeMailSender.title).isEqualTo("Please certify your email address");
        assertThat(fakeMailSender.content).isEqualTo("Please click the following link to certify your email address: ");
    }

}