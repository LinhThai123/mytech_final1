package com.example.mytech.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserCourseDTO {

    private String id;

    private String userName;

    private String courseName;

    private Timestamp enrollDate;

    private int status;
}
