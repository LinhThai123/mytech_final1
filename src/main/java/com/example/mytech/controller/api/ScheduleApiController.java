package com.example.mytech.controller.api;

import com.example.mytech.entity.Course;
import com.example.mytech.entity.Schedule;
import com.example.mytech.model.dto.ScheduleDTO;
import com.example.mytech.model.request.CourseRep;
import com.example.mytech.model.request.ScheduleReq;
import com.example.mytech.service.CourseService;
import com.example.mytech.service.ScheduleService;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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


    @GetMapping("api/schedules/{courseId}")
    public ResponseEntity<String> getCourseSchedulesJson(@PathVariable("courseId") String courseId, @RequestParam(name = "weeks", defaultValue = "4") int numberOfWeeks) {
        Course course = courseService.getCourseById(courseId);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }

        List<Schedule> weeklySchedules = course.generateWeeklySchedules(numberOfWeeks);
        String json = new Gson().toJson(weeklySchedules);

        return ResponseEntity.ok(json);
    }

    @GetMapping("/courses/{courseId}/schedules")
    public ResponseEntity<List<Schedule>> getCourseSchedules(@PathVariable("courseId") String courseId) {
        Course course = courseService.getCourseById(courseId);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }

        List<Schedule> schedules = new ArrayList<>();

        // Tạo lịch học cho từng tuần
        LocalDate currentDate = course.getStartDate();
        while (!currentDate.isAfter(course.getEndDate())) {
            DayOfWeek currentDayOfWeek = currentDate.getDayOfWeek();

            for (Schedule schedule : course.getSchedules()) {
                if (schedule.getDayOfWeek().toDayOfWeek() == currentDayOfWeek) {
                    LocalDateTime startDateTime = LocalDateTime.of(currentDate, LocalTime.parse(schedule.getStartTime()));
                    LocalDateTime endDateTime = LocalDateTime.of(currentDate, LocalTime.parse(schedule.getEndTime()));

                    Schedule newSchedule = new Schedule();
                    newSchedule.setId(UUID.randomUUID().toString());
                    newSchedule.setDayOfWeek(schedule.getDayOfWeek());
                    newSchedule.setStartTime(startDateTime.toString());
                    newSchedule.setEndTime(endDateTime.toString());

                    schedules.add(newSchedule);
                }
            }

            currentDate = currentDate.plusDays(1);
        }

        return ResponseEntity.ok(schedules);
    }

    @DeleteMapping("/courses/{courseId}/schedules/{scheduleId}")
    public ResponseEntity<?> deleteCourseSchedule(@PathVariable("courseId") String courseId, @PathVariable("scheduleId") String scheduleId) {
        Course course = courseService.getCourseById(courseId);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }

        Schedule schedule = scheduleService.getScheduleById(scheduleId);
        if (schedule == null) {
            return ResponseEntity.notFound().build();
        }

        course.getSchedules().remove(schedule);
        courseService.saveCourse(course);

        return ResponseEntity.ok().build();
    }

}
