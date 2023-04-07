package com.example.mytech.controller.api;

import com.example.mytech.entity.Schedule;
import com.example.mytech.model.dto.ScheduleDTO;
import com.example.mytech.model.request.CourseRep;
import com.example.mytech.model.request.ScheduleReq;
import com.example.mytech.service.CourseService;
import com.example.mytech.service.ScheduleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class ScheduleApiController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private CourseService courseService ;

    ModelMapper modelMapper = new ModelMapper();

    @GetMapping("admin/schedules")
    public String getAdminSchedule(Model model,
                                   @RequestParam(defaultValue = "1", required = false) Integer page) {

        Page<ScheduleDTO> schedules = scheduleService.findScheduleByCourseName(page);
        model.addAttribute("schedules", schedules.getContent());
        model.addAttribute("totalPages", schedules.getTotalPages());
        model.addAttribute("currentPage", schedules.getPageable().getPageNumber() + 1);
        return "admin/schedule/list";
    }

    @ModelAttribute("courses")
    public List<CourseRep> getCourses() {
        return courseService.getListCourse().stream()
                .map(item -> {
                    CourseRep rep = new CourseRep();
                    modelMapper.map(item, rep);
                    return rep;
                }).collect(Collectors.toList());
    }

    @PostMapping("/api/schedules")
    public ResponseEntity<?> createSchedule (@Valid @RequestBody ScheduleReq req) {
        Schedule schedule = scheduleService.createSchedule(req);
        return ResponseEntity.ok().body(schedule);
    }

    @PutMapping("/api/update/schedules/{id}")
    public ResponseEntity<?> updateSchedule (@PathVariable String id , @RequestBody ScheduleReq req) {
        scheduleService.updateSchedule(id, req);
        return ResponseEntity.ok("Cập nhật lịch học thành công");
    }

    // delete schedule api
    @DeleteMapping("/api/delete/schedule/{id}")
    public ResponseEntity<?> deleteCourse (@PathVariable("id") String id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.ok("Xóa thàng công");
    }
}
