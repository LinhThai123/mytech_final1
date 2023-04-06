package com.example.mytech.service;

import com.example.mytech.entity.Schedule;
import com.example.mytech.entity.User;
import com.example.mytech.model.request.ScheduleReq;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface ScheduleService {

    Page<Schedule> findScheduleByCourse_NameContaining(String id, String nameCourse, Integer page);

    public Schedule createSchedule (String courseId , ScheduleReq req) ;

    public Schedule updateSchedule (String id, ScheduleReq req) ;

    public void deleteSchedule (String id) ;
}
