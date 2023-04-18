package com.example.mytech.service.impl;

import com.example.mytech.config.Contant;
import com.example.mytech.entity.Course;
import com.example.mytech.entity.Schedule;
import com.example.mytech.exception.InternalServerException;
import com.example.mytech.exception.NotFoundException;
import com.example.mytech.model.dto.ScheduleDTO;
import com.example.mytech.model.request.ScheduleReq;
import com.example.mytech.repository.ScheduleRepository;
import com.example.mytech.service.CourseService;
import com.example.mytech.service.ScheduleService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;

@Component
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository ;

    @Autowired
    private CourseService courseService ;

    @Override
    public List<Schedule> getListSchedule() {
        return scheduleRepository.findAll();
    }

    @Override
    public Page<ScheduleDTO> findScheduleByCourseName( Integer page) {
        page--;
        if (page < 0) {
            page = 0;
        }
        Pageable pageable = PageRequest.of(page, Contant.LIMIT_COURSE, Sort.by("day").descending());
        return scheduleRepository.findScheduleByCourseName(pageable);
    }

    @SneakyThrows
    @Override
    public Schedule createSchedule(ScheduleReq req) throws ParseException {

        // Kiểm tra mã khóa học
        if (req.getCourse_id().isEmpty()){
            throw new NotFoundException("Không tìm thấy khóa học ");
        }

        // Lấy khóa học từ mã khóa học
        Course course = courseService.getCourseById(req.getCourse_id());

        // Tạo đối tượng lịch học mới
        Schedule schedule = new Schedule();
        schedule.setCourse(course);

        // Chuyển đổi ngày học từ chuỗi sang Date
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        Date reqDate = formatter.parse(String.valueOf(req.getDay()));

        Date courseStartDate = formatter.parse(String.valueOf(course.getStartDate())) ;


        if (reqDate.before(courseStartDate)) {
            throw new IllegalArgumentException("Ngày học không nằm trong thời gian bắt đầu của khóa học. "
                    + courseStartDate);
        }
        schedule.setDay(reqDate);
        schedule.setDayOfWeek(req.getDayOfWeek());
        schedule.setDuration(req.getDuration());

        if (req.getStatus() == null) {
            schedule.setStatus(1);
        } else {
            schedule.setStatus(req.getStatus());
        }

        // Kiểm tra lịch học đã tồn tại chưa
        List<Schedule> existingSchedules = scheduleRepository.findByDayOrCourse(reqDate , course);
        for (Schedule existingSchedule : existingSchedules) {
            String existingDayString = formatter.format(existingSchedule.getDay());
            Date existingDate = formatter.parse(existingDayString);
            if (existingDate.equals(reqDate) && existingSchedule.getCourse().getName().equals(schedule.getCourse().getName())) {
                throw new IllegalArgumentException("Lịch học đã tồn tại.");
            }
        }
        try {
            scheduleRepository.save(schedule);
        } catch (Exception e) {
            throw new InternalServerException("Tạo lịch học cho khóa học thất bại ");
        }
        return schedule;
    }

    @SneakyThrows
    @Override
    public Schedule updateSchedule(String id, ScheduleReq req) {
        Schedule schedule ;
        Optional<Schedule> rs = scheduleRepository.findById(id);
        schedule = rs.get();
        if(!rs.isPresent()) {
            throw new NotFoundException("Không tìm thấy lịch học ");
        }

        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        Date reqDate = formatter.parse(String.valueOf(req.getDay()));
        schedule.setDay(reqDate);

        schedule.setDayOfWeek(DayOfWeek.valueOf(String.valueOf(req.getDayOfWeek())));
        schedule.setDuration(req.getDuration());
        schedule.setStatus(req.getStatus());
        try {
            scheduleRepository.save(schedule);
        } catch (Exception e) {
            throw new InternalServerException("Cập nhật lịch học cho khóa học thất bại ");
        }
        return schedule;
    }

    @Override
    public void deleteSchedule(Schedule schedule) {
        scheduleRepository.delete(schedule);
    }

    @Override
    public Schedule getScheduleById(String id) {
        Optional<Schedule> schedule = scheduleRepository.findById(id);
        if (!schedule.isPresent()) {
            throw new NotFoundException("Schedule do not exits");
        }
        return schedule.get();

    }

    @Override
    public List<Schedule> getCourseSchedules(String courseId) {
        // Tìm khóa học theo Id
        Course course = courseService.getCourseById(courseId);
        if (course == null) {
            throw new NotFoundException("Không tìm thấy khóa học");
        }
        // Lấy danh sách lịch học theo khóa học
        List<Schedule> scheduleList = scheduleRepository.findByCourse(course);

        return scheduleList;
    }
}
