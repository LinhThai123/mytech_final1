package com.example.mytech.service;

import com.example.mytech.entity.User;
import com.example.mytech.model.dto.AttendanceDTO;
import com.example.mytech.model.dto.AttendanceResponseDTO;
import com.example.mytech.model.dto.CourseResponseDTO;
import com.example.mytech.model.dto.ScheduleResponseDTO;
import com.example.mytech.utils.PageUtil;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AttendanceService {

    public List<AttendanceDTO> getAttendanceListForCourse(String courseId);

    public void markAttendance(List<AttendanceDTO> attendanceList) ;

//   public List<AttendanceResponseDTO> getAttendanceListByCourseId (String courseId);


    public CourseResponseDTO getAttendanceListByCourseId (String courseId) ;

    public ScheduleResponseDTO getUserOfCourseByScheduleId (String scheduleId);

    public List<User> getUsersByScheduleId(String scheduleId) ;

}
