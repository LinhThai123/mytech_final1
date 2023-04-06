package com.example.mytech.controller.admin;


import com.example.mytech.entity.Course;
import com.example.mytech.entity.Schedule;
import com.example.mytech.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService ;

    @GetMapping("/schedules")
    public String getAdminProducts(Model model,
                                   @RequestParam(defaultValue = "", required = false) String id,
                                   @RequestParam(defaultValue = "", required = false) String nameCourse,
                                   @RequestParam(defaultValue = "1", required = false) Integer page) {
        // get list course
        Page<Schedule> schedules = scheduleService.findScheduleByCourse_NameContaining(id, nameCourse, page);
        model.addAttribute("schedules", schedules.getContent());
        model.addAttribute("totalPages", schedules.getTotalPages());
        model.addAttribute("currentPage", schedules.getPageable().getPageNumber() + 1);
        return "admin/schedule/list";
    }
}
