package com.example.mytech.controller.api;


import com.example.mytech.entity.User;
import com.example.mytech.model.request.UserRep;
import com.example.mytech.repository.UserRepository;
import com.example.mytech.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.WebSocketHandler;

import java.util.List;


@RestController
@RequestMapping("/api")
public class UserApiController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    // get all user have role sutdent
    @GetMapping("/student")
    public List<User> getAllStudents() {
        List<User> allStudent = userService.getUserWithRoleUser();
        return allStudent;
    }

    //get all user
    @GetMapping("/users")
    public List<User> getAllUsers() {
        List<User> allUsers = userRepository.findAll();
        return allUsers;
    }
    // create user
    @PostMapping("/admin/user")
    public ResponseEntity<?> RegisterUser(@Validated @RequestBody UserRep rep) {
        userService.createUser(rep) ;
        return ResponseEntity.ok("Đăng ký thành công");
    }
    // update user
    @PutMapping("/admin/update/user/{id}")
    public ResponseEntity<?> updateUser (@Validated @PathVariable("id") String id , UserRep rep) {
        userService.updateUser(id,rep);
        return ResponseEntity.ok("Cập nhật thành công");
    }
}
