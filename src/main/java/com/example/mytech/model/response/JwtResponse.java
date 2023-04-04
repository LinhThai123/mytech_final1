package com.example.mytech.model.response;

import com.example.mytech.entity.UserCourse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JwtResponse {
    private String token ;
    private String type = "Bearer" ;
    private String id ;
    private String name ;
    private String email ;
    private List<String> roles ;
    private List<Map<String, Object>> courseLists;

    public JwtResponse(String token, String id, String name, String email, List<String> roles, List<UserCourse> userCourses) {
        this.token = token;
        this.id = id;
        this.name = name;
        this.email = email;
        this.roles = roles;
        this.courseLists = new ArrayList<>();

        for (UserCourse userCourse : userCourses) {
            Map<String, Object> courseMap = new HashMap<>();
            courseMap.put("course_id", userCourse.getCourseId());
            courseMap.put("enroll_date", userCourse.getEnrollDate());
            courseMap.put("status", userCourse.getStatus());
            this.courseLists.add(courseMap);
        }
    }

    public JwtResponse(String jwt, String username, String password, List<String> roles) {
    }
}
