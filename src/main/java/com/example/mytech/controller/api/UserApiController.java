package com.example.mytech.controller.api;


import com.example.mytech.entity.Course;
import com.example.mytech.entity.User;
import com.example.mytech.model.request.UpdateProfileReq;
import com.example.mytech.model.request.UserRep;
import com.example.mytech.repository.UserRepository;
import com.example.mytech.security.CustomUserDetails;
import com.example.mytech.service.CourseService;
import com.example.mytech.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.socket.WebSocketHandler;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api")
public class UserApiController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseService courseService ;

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

    // get profile
    @GetMapping("/profile")
    public ResponseEntity<?> getUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            Optional<User> optionalUser = Optional.ofNullable(userRepository.findByEmail(userDetails.getUsername()));
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                return ResponseEntity.ok().body(user);
            }
        }
        return  ResponseEntity.badRequest().body("Bạn cần đăng nhập để xem thông tin ");
    }

    // update profile
    @PostMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody UpdateProfileReq req) {
        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        user = userService.updateProfile(user, req);
        UserDetails principal = new CustomUserDetails(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ResponseEntity.ok("Cập nhật profile thành công");
    }

    // get list course of user isLogined
    @GetMapping("/course/users/{userId}")
    public List<Course> getCourseByUserId (@PathVariable String userId) {
        return courseService.findCoursesByUserId(userId) ;
    }
}
