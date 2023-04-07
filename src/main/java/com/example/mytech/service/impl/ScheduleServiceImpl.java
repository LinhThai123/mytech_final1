package com.example.mytech.service.impl;


import com.example.mytech.config.Contant;
import com.example.mytech.entity.Course;
import com.example.mytech.entity.Day;
import com.example.mytech.entity.Schedule;
import com.example.mytech.exception.InternalServerException;
import com.example.mytech.exception.NotFoundException;
import com.example.mytech.model.dto.ScheduleDTO;
import com.example.mytech.model.request.ScheduleReq;
import com.example.mytech.repository.ScheduleRepository;
import com.example.mytech.service.CourseService;
import com.example.mytech.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository ;

    @Autowired
    private CourseService courseService ;

    @Override
    public Page<ScheduleDTO> findScheduleByCourseName( Integer page) {
        page--;
        if (page < 0) {
            page = 0;
        }
        Pageable pageable = PageRequest.of(page, Contant.LIMIT_COURSE, Sort.by("startTime").descending());
        return scheduleRepository.findScheduleByCourseName(pageable);
    }

    @Override
    public Schedule createSchedule(ScheduleReq req) {

        Schedule schedule = new Schedule();

        if (req.getDayOfWeek() == null) {
            throw new IllegalArgumentException("Giá trị daysOfWeek không được để trống");
        }
        schedule.setDayOfWeek(Day.valueOf(String.valueOf(req.getDayOfWeek())));

        if (req.getCourse_id().isEmpty()){
            throw new NotFoundException("Không tìm thấy khóa học ");
        }

        // Kiểm tra số lượng lịch học hiện tại của khóa học
        List<Schedule> existingSchedules = scheduleRepository.findByCourseId(req.getCourse_id());
        if (existingSchedules.size() >= 3) {
            throw new IllegalArgumentException("Không thể thêm quá 3 lịch học cho cùng một khóa học");
        }

        // Kiểm tra trùng ngày học
        for (Schedule existingSchedule : existingSchedules) {
            if (existingSchedule.getDayOfWeek() == schedule.getDayOfWeek()) {
                throw new IllegalArgumentException("Ngày học đã tồn tại trong lịch học của khóa học này");
            }
        }

        Course course = courseService.getCourseById(req.getCourse_id());
        schedule.setCourse(course);
        schedule.setStartTime(req.getStartTime());
        schedule.setEndTime(req.getEndTime());

        try {
            scheduleRepository.save(schedule);
        } catch (Exception e) {
            throw new InternalServerException("Tạo lịch học cho khóa học thất bại ");
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

    @Override
    public Schedule getScheduleById(String id) {
        Optional<Schedule> schedule = scheduleRepository.findById(id);
        if (!schedule.isPresent()) {
            throw new NotFoundException("Schedule do not exits");
        }
        return schedule.get();

    }
}
