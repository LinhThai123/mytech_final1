package com.example.mytech.controller.api;

import com.example.mytech.exception.NotFoundException;
import com.example.mytech.model.dto.AttendanceDTO;
import com.example.mytech.model.dto.CourseResponseDTO;
import com.example.mytech.model.dto.ScheduleResponseDTO;
import com.example.mytech.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AttendanceApiController {

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("/course/{courseId}")
    public ResponseEntity<CourseResponseDTO> getAttendanceListByCourseId(@PathVariable String courseId) {
        try {
            CourseResponseDTO attendanceResponseList = attendanceService.getAttendanceListByCourseId(courseId);
            return ResponseEntity.ok(attendanceResponseList);
        } catch (NotFoundException e) {
            // Xử lý ngoại lệ NotFoundException
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // Xử lý ngoại lệ chung
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/schedule/{scheduleId}")
    public ResponseEntity<ScheduleResponseDTO> getAttendanceListByScheduleId(@PathVariable String scheduleId) {
        try {
            ScheduleResponseDTO attendanceResponseList = attendanceService.getUserOfCourseByScheduleId(scheduleId);
            return ResponseEntity.ok(attendanceResponseList);
        } catch (NotFoundException e) {
            // Xử lý ngoại lệ NotFoundException
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // Xử lý ngoại lệ chung
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/api/attendance/mark")
    public ResponseEntity<String> markAttendance(@RequestBody List<AttendanceDTO> attendanceList) {
        try {
            attendanceService.markAttendance(attendanceList);
            return ResponseEntity.ok("Tạo ra danh sách điểm danh thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Tạo ra danh sách điểm danh thất bại: " + e.getMessage());
        }
    }
}
