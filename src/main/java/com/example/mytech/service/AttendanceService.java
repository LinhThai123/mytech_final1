package com.example.mytech.service;

import com.example.mytech.entity.Attendance;
import com.example.mytech.entity.User;
import com.example.mytech.model.dto.AttendanceDTO;
import com.example.mytech.model.request.ChangeAttendanceReq;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AttendanceService {

    public void getUserOfCourseByScheduleId (String scheduleId);

    public List<User> getUsersByScheduleId(String scheduleId) ;

    public List<AttendanceDTO> getUserAndAttendanceByScheduleId (String scheduleId) ;

    public void updateAttendance(List<AttendanceDTO> attendanceDTOs);

    List<Attendance> findByAttendanceIsTrue();

    List<Attendance> findByAttendanceIsFalse();

}
