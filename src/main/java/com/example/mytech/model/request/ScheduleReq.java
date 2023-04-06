package com.example.mytech.model.request;

import com.example.mytech.entity.Day;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ScheduleReq {

    private String id;

    private Day dayOfWeek;

    private String startTime;

    private String endTime;

    private String course_id;
}
