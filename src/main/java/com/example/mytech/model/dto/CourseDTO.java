package com.example.mytech.model.dto;

import com.example.mytech.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

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
    private int numberOfSessions ;
    private String totalTime;
    private List<String> teacheNames;
    private List<Schedule> scheduleList ;

    public List<String> getTeacheNames(List<String> teacherNames) {
        return teacheNames;
    }

    public void setTeacheNames(List<String> teacheNames) {
        this.teacheNames = teacheNames;
    }
}
