package com.example.mytech.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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
    private List<String> courseIds;

    public JwtResponse(String token, String id, String name, String email, List<String> roles , List<String> courseIds) {
        this.token = token;
        this.id = id;
        this.name = name;
        this.email = email;
        this.roles = roles;
        this.courseIds = courseIds ;
    }

    public JwtResponse(String jwt, String username, String password, List<String> roles) {
    }
}
