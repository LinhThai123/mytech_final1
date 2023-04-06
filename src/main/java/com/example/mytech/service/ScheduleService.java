package com.example.mytech.service;

import com.example.mytech.entity.Schedule;
import com.example.mytech.model.request.ScheduleReq;
import org.springframework.stereotype.Service;

@Service
public interface ScheduleService {

    public Schedule createSchedule (String courseId , ScheduleReq req) ;

    public Schedule updateSchedule (String id, ScheduleReq req) ;
}
