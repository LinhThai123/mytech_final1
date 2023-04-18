package com.example.mytech.controller.admin;

import com.example.mytech.model.dto.AttendanceResponseDTO;
import com.example.mytech.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService ;

//    @GetMapping("/attendance/course/{courseId}")
//    public String getAttendanceListByCourseId(@PathVariable String courseId, Model model) {
//        List<AttendanceResponseDTO> attendanceResponseList = attendanceService.getAttendanceListByCourseId(courseId);
//        model.addAttribute("attendanceList", attendanceResponseList);
//        return "/admin/attendance/list";
//    }
}
