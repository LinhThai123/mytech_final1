package com.example.mytech.service;

import com.example.mytech.entity.Schedule;
import com.example.mytech.model.dto.ScheduleDTO;
import com.example.mytech.model.request.ScheduleReq;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface ScheduleService {

    Page<ScheduleDTO> findScheduleByCourseName (Integer page);

    public Schedule createSchedule ( ScheduleReq req) ;

    public Schedule updateSchedule (String id, ScheduleReq req) ;

    public void deleteSchedule (String id) ;

    public Schedule getScheduleById (String id) ;

    // Thêm mới hoặc cập nhật một lịch học
    Schedule saveSchedule(Schedule schedule);
}
