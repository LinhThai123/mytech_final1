package com.example.mytech.controller.api;

import com.example.mytech.entity.Attendance;
import com.example.mytech.exception.NotFoundException;
import com.example.mytech.model.dto.AttendanceDTO;
import com.example.mytech.model.request.ChangeAttendanceReq;
import com.example.mytech.repository.AttendanceRepository;
import com.example.mytech.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AttendanceApiController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private AttendanceRepository attendanceRepository;

    // tạo danh sách điểm danh của học viên theo lịch học
    @GetMapping("/schedule/{scheduleId}")
    public ResponseEntity<?> getUserOfCourseListByScheduleId(@PathVariable String scheduleId) {
        try {
             attendanceService.getUserOfCourseByScheduleId(scheduleId);
            return ResponseEntity.ok("Tạo danh sách điểm danh cho học viên thành công");
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // lấy ra danh sách học sinh đã tạo
    @GetMapping("/attendance/{scheduleId}")
    public ResponseEntity<?> getUserAndAttendanceBySchedule (@PathVariable String scheduleId) {
        try {
           List<AttendanceDTO> attendanceDTOS = attendanceService.getUserAndAttendanceByScheduleId(scheduleId) ;
            return ResponseEntity.ok(attendanceDTOS);
        }
        catch (NotFoundException e) {
            return ResponseEntity.notFound().build() ;
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build() ;
        }
    }

    // thực hiện chức năng điểm danh
    @PutMapping("/update-attendance")
    public ResponseEntity<?> updateAttendance(@RequestBody List<AttendanceDTO> attendanceDTOs) {
        try {
            attendanceService.updateAttendance(attendanceDTOs);
            return ResponseEntity.ok("Điểm danh thành công");
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // lấy ra danh sach học viên đã điểm danh
    @GetMapping("/attended/true")
    public ResponseEntity<List<Attendance>> getAttendancesWithTrueAttendance() {
        List<Attendance> attendances = attendanceService.findByAttendanceIsTrue();
        return ResponseEntity.ok(attendances);
    }

    // lấy ra danh sách học viên chưa điểm danh
    @GetMapping("/attended/false")
    public ResponseEntity<List<Attendance>> getAttendancesWithFalseAttendance() {
        List<Attendance> attendances = attendanceService.findByAttendanceIsFalse();
        return ResponseEntity.ok(attendances);
    }

}
