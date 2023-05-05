package com.example.mytech.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RankUserDTO {

    private String name ;

    private String image;

    private float midtermGrades;

    private float finalGrades;

    private float exams;

    private  float avg;

    private String ranking;
}
