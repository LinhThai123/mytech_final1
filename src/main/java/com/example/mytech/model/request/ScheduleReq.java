package com.example.mytech.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ScheduleReq {

    private String id;

    private String dayOfWeek;

    private String startTime;

    private String endTime;

    private String course_id;
}
