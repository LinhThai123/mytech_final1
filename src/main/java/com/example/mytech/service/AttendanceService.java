package com.example.mytech.service;

import com.example.mytech.entity.Attendance;
import com.example.mytech.entity.User;
import com.example.mytech.model.dto.ScheduleResponseDTO;
import com.example.mytech.model.request.ChangeAttendanceReq;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AttendanceService {

    public List<Attendance> getAttendanceListByScheduleId(String scheduleId) ;

    public void getUserOfCourseByScheduleId (String scheduleId);

    public List<User> getUsersByScheduleId(String scheduleId) ;

    public Attendance updateAttendanceStatus(String attendanceId, ChangeAttendanceReq req);

    List<Attendance> findByAttendanceIsTrue();

    List<Attendance> findByAttendanceIsFalse();

}
