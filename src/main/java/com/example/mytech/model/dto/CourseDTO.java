package com.example.mytech.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CourseDTO {

    private String id ;
    private String name ;
    private String description ;
    private int status ;
    private int active ;
    private Long price ;
    private int level ;
    private String image;
    private Timestamp publishedAt;
    private Date expiredAt;

}
