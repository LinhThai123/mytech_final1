package com.example.mytech.controller.api;

import com.example.mytech.entity.Course;
import com.example.mytech.entity.Schedule;
import com.example.mytech.entity.User;
import com.example.mytech.model.request.CourseRep;
import com.example.mytech.model.request.ScheduleReq;
import com.example.mytech.service.CourseService;
import com.example.mytech.service.ScheduleService;
import com.example.mytech.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CourseApiController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService ;

    @Autowired
    private ScheduleService scheduleService;

    //get list course
    @GetMapping ("/admin/course/list")
    public ResponseEntity<Object> getListCourse () {
        List<Course> course = courseService.getListCourse();
        return ResponseEntity.ok(course);
    }

    //add course api
    @PostMapping("/admin/courses")
    public ResponseEntity<?> createCourse(@Valid @RequestBody CourseRep req) {
        Course course = courseService.createCourse(req);
        return ResponseEntity.ok(course);
    }

    // update course api
    @PutMapping("/admin/courses/update/{id}")
    public ResponseEntity<?> updateCourse (@PathVariable("id") String id, @Valid @RequestBody CourseRep rep) {
        courseService.updateCourse(id, rep);
        return ResponseEntity.ok("Cập nhật thành công");
    }

    // delete course api
    @DeleteMapping("/admin/courses/delete/{id}")
    public ResponseEntity<?> deleteCourse (@PathVariable("id") Course id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok("Xóa thàng công");
    }

    // get list role_user by course id
    @GetMapping("/course/{id}/users/role_user")
    public List<User> getUsersWithRoleUserInCourse(@PathVariable("id") String courseId) {
        return userService.findUsersWithRoleUserInCourse(courseId);
    }

    // get list role_teacher by course id
    @GetMapping("/course/{id}/users/role_teacher")
    public List<User> findUsersWithRoleTeacherInCourse (@PathVariable("id") String courseId) {
        return userService.findUsersWithRoleTeacherInCourse(courseId);
    }

    // get list course by id
    @GetMapping("/course/{id}")
    public ResponseEntity<?> getListCourseById (@PathVariable String id){
         Course course = courseService.getCourseById(id);
         return ResponseEntity.ok(course);
    }

}
