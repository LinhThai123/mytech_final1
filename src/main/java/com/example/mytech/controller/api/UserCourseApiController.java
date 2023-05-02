package com.example.mytech.controller.api;

import com.example.mytech.model.dto.UserCourseDTO;
import com.example.mytech.model.request.ChangeStatusReq;
import com.example.mytech.repository.UserCourseRepository;
import com.example.mytech.service.UserCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@Controller
public class UserCourseApiController {

    @Autowired
    private UserCourseService userCourseService;

    @Autowired
    private UserCourseRepository userCourseRepository;

    // admin duyệt user đăng ký khóa học
    @PutMapping("/{id}/status")
    public ResponseEntity<Object> updateStatus(@PathVariable("id") String id, @RequestBody ChangeStatusReq req) {
        userCourseService.updateStatus(id, req);
        return ResponseEntity.ok("Cập nhật thành công");
    }

    @GetMapping("/usercourse/{userId}")
    @ResponseBody
    public List<UserCourseDTO> getUserCourses( @PathVariable String userId) {
        return userCourseService.findByUserId(userId);
    }

    @PutMapping("/notification/{userId}")
    public ResponseEntity<?> updateTokenNotification(@PathVariable String userId , @RequestParam String tokenNotification) {
        userCourseService.updateTokenNotification(userId, tokenNotification);
        return ResponseEntity.ok("TokenNotification updated successfully");
    }

    @GetMapping("/users/status")
    public ResponseEntity<List<UserCourseDTO>> getUserCoursesByStatus(@RequestParam(value = "status", defaultValue = "0") int status) {
        List<UserCourseDTO> userCourses = userCourseService.getUserCoursesByStatus(status);
        return ResponseEntity.ok(userCourses);
    }
}
