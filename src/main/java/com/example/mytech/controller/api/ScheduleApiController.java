package com.example.mytech.controller.api;

import com.example.mytech.entity.Schedule;
import com.example.mytech.model.request.ScheduleReq;
import com.example.mytech.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ScheduleApiController {

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping("/courses/{courseId}/schedules")
    public ResponseEntity<?> createSchedule (@PathVariable String courseId , @RequestBody ScheduleReq req) {
        Schedule schedule = scheduleService.createSchedule(courseId,req);
        return ResponseEntity.ok().body(schedule);
    }

    @PutMapping("/update/schedules/{id}")
    public ResponseEntity<?> updateSchedule (@PathVariable String id , @RequestBody ScheduleReq req) {
        scheduleService.updateSchedule(id, req);
        return ResponseEntity.ok("Cập nhật lịch học thành công");
    }
}
