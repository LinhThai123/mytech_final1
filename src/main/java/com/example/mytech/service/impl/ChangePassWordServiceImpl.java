package com.example.mytech.service.impl;

import com.example.mytech.entity.User;
import com.example.mytech.model.request.ChangePassWordRep;
import com.example.mytech.repository.UserRepository;
import com.example.mytech.service.ChangePassWordService;
import com.example.mytech.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ChangePassWordServiceImpl implements ChangePassWordService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder ;

    @Override
    public void changePassWord(String id, ChangePassWordRep rep) throws Exception {
        User user = userRepository.findById(id).get();

        if (user == null) {
            throw new Exception("User not found");
        }

        if (!passwordEncoder.matches(rep.getOldpassword(), user.getPassword())) {
            throw new Exception("Invalid old password");
        }

        if (!rep.getNewpassword().equals(rep.getRetypepassword())) {
            throw new Exception("New password and retype password are not matched");
        }
        user.setPassword(passwordEncoder.encode(rep.getNewpassword()));
        userRepository.save(user);
    }
}
