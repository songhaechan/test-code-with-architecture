package com.example.demo.mock;

import com.example.demo.user.service.port.MailSender;

public class FakeMailSender implements MailSender {
    public String email;
    public String title;
    public String content;


    @Override
    public void send(final String email, final String title, final String content) {
        this.email = email;
        this.title = title;
        this.content = content;
    }
}
