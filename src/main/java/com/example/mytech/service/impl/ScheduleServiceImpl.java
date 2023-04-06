package com.example.mytech.service.impl;


import com.example.mytech.config.Contant;
import com.example.mytech.entity.Course;
import com.example.mytech.entity.Day;
import com.example.mytech.entity.Schedule;
import com.example.mytech.exception.InternalServerException;
import com.example.mytech.exception.NotFoundException;
import com.example.mytech.model.request.ScheduleReq;
import com.example.mytech.repository.CourseRepository;
import com.example.mytech.repository.ScheduleRepository;
import com.example.mytech.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository ;

    @Autowired
    private CourseRepository courseRepository ;

    @Override
    public Page<Schedule> findScheduleByCourse_NameContaining(String id, String nameCourse, Integer page) {
        page--;
        if (page < 0) {
            page = 0;
        }
        Pageable pageable = PageRequest.of(page, Contant.LIMIT_COURSE);
        return scheduleRepository.findScheduleByIdOrCourse_NameContaining(id,nameCourse,pageable);
    }

    @Override
    public Schedule createSchedule(String courseId ,ScheduleReq req) {
        Schedule schedule = new Schedule();
        Optional<Course> course = courseRepository.findById(courseId);
        if (!course.isPresent()) {
            throw new NotFoundException("Không tìm thấy khóa học");
        }

        // Kiểm tra giá trị null của daysOfWeek trước khi thực hiện chuyển đổi sang đối tượng enum
        if (req.getDayOfWeek() == null) {
            throw new IllegalArgumentException("Giá trị daysOfWeek không được để trống");
        }

        schedule.setDayOfWeek(Day.valueOf(String.valueOf(req.getDayOfWeek())));
        schedule.setCourse(course.get());
        schedule.setStartTime(req.getStartTime());
        schedule.setEndTime(req.getEndTime());

        try {
            scheduleRepository.save(schedule);
        } catch (Exception e) {
            throw new InternalServerException("Tạo lịch học cho khóa học " + courseId + " thất bại ");
        }
        return schedule;
    }

    @Override
    public Schedule updateSchedule(String id, ScheduleReq req) {
        Schedule schedule ;
        Optional<Schedule> rs = scheduleRepository.findById(id);
        schedule = rs.get();
        if(!rs.isPresent()) {
            throw new NotFoundException("Không tìm thấy lịch học ");
        }
        schedule.setDayOfWeek(Day.valueOf(String.valueOf(req.getDayOfWeek())));

        schedule.setStartTime(req.getStartTime());
        schedule.setEndTime(req.getEndTime());

        try {
            scheduleRepository.save(schedule);
        } catch (Exception e) {
            throw new InternalServerException("Cập nhật lịch học cho khóa học thất bại ");
        }
        return schedule;
    }

    @Override
    public void deleteSchedule(String id) {
        Optional<Schedule> rs = scheduleRepository.findById(id) ;
        if(!rs.isPresent()) {
            throw new NotFoundException("Không tìm thấy lịch học có " + id) ;
        }
        scheduleRepository.deleteById(id);
    }
}
