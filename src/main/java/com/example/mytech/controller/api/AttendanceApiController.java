package com.example.mytech.controller.api;

import com.example.mytech.entity.Attendance;
import com.example.mytech.exception.NotFoundException;
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

@Controller
public class AttendanceApiController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private AttendanceRepository attendanceRepository;


    // xem danh sách điểm danh của học viên theo lịch học
    @GetMapping("/attendance/{scheduleId}")
    public ResponseEntity<List<Attendance>> getAttendanceListByScheduleId(@PathVariable String scheduleId) {
        List<Attendance> attendanceList = attendanceService.getAttendanceListByScheduleId(scheduleId);
        return ResponseEntity.ok(attendanceList);
    }

    // tạo danh sách điểm danh của học viên theo lịch học
    @GetMapping("/schedule/{scheduleId}")
    public ResponseEntity<?> getUserOfCourseListByScheduleId(@PathVariable String scheduleId) {
        try {
             attendanceService.getUserOfCourseByScheduleId(scheduleId);
            return ResponseEntity.ok("Tạo danh sách điểm danh cho học viên thành công");
        } catch (NotFoundException e) {
            // Xử lý ngoại lệ NotFoundException
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // Xử lý ngoại lệ chung
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // điêm danh cho cả 1 danh sách học viên theo lịch học
    @PutMapping("/update-attendance")
    public ResponseEntity<List<Attendance>> updateAttendanceStatus(@RequestBody List<ChangeAttendanceReq> reqs) {
        List<Attendance> updatedAttendances = new ArrayList<>();
        for (ChangeAttendanceReq request : reqs) {
            List<String> attendanceIds = request.getAttendanceIds();
            for (String attendanceId : attendanceIds) {
                Attendance attendance = attendanceRepository.findById(attendanceId).orElse(null);
                if (attendance == null) {
                    throw new RuntimeException("Không tìm thấy đối tượng điểm danh");
                }
                attendance.setAttendance(request.isAttendance());
                updatedAttendances.add(attendanceRepository.save(attendance));
            }
        }
        return ResponseEntity.ok(updatedAttendances);
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
