package com.example.mytech.service.impl;

import com.example.mytech.entity.User;
import com.example.mytech.repository.UserRepository;
import com.example.mytech.service.ForgotPassWordService;
import com.example.mytech.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ForgotPassWordServiceImpl implements ForgotPassWordService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder ;

    @Autowired
    private MailService mailService;


    @Override
    public String generateNewPassword() {
        return UUID.randomUUID().toString();
    }
}
