package com.example.mytech.service.impl;

import com.example.mytech.config.Contant;
import com.example.mytech.entity.Course;
import com.example.mytech.entity.Schedule;
import com.example.mytech.entity.User;
import com.example.mytech.entity.UserCourse;
import com.example.mytech.exception.NotFoundException;
import com.example.mytech.model.dto.CourseDTO;
import com.example.mytech.model.dto.UserCourseDTO;
import com.example.mytech.model.request.ChangeStatusReq;
import com.example.mytech.notification.NotificationService;
import com.example.mytech.repository.UserCourseRepository;
import com.example.mytech.service.CourseService;
import com.example.mytech.service.UserCourseService;
import com.example.mytech.service.UserService;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
public class UserCourseServiceImpl implements UserCourseService {

    @Autowired
    private UserCourseRepository userCourseRepository;

    @Autowired
    private NotificationService service;

    @Autowired
    private CourseService courseService ;

    @Autowired
    private UserService userService;

    @Override
    public List<UserCourseDTO> findByUserId(String id) {
        List<UserCourseDTO> dtos = new ArrayList<>();
        List<UserCourse> userCourses = userCourseRepository.findByUser_Id(id);
        for (UserCourse userCourse : userCourses) {
            UserCourseDTO dto = new UserCourseDTO();
            dto.setCourseId(userCourse.getCourse().getId());
            dto.setEnrollDate(Timestamp.valueOf(userCourse.getEnrollDate().toString()));
            dto.setStatus(userCourse.getStatus());
            dto.setImage(userCourse.getCourse().getImage());
            dto.setName(userCourse.getCourse().getName());
            dto.setAddress(userCourse.getCourse().getAddress());
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public UserCourse getUserCourseById(String id) {
        Optional<UserCourse> rs = userCourseRepository.findById(id);
        if (!rs.isPresent()) {
            throw new NotFoundException("UserCourse do not exits");
        }
        return rs.get();
    }

    // cập nhật token vào UserCourse
    @Override
    public void updateTokenNotification(String userId, String tokenNotification) {
        List<UserCourse> userCourses = userCourseRepository.findByUser_Id(userId);
        userCourses.forEach(userCourse -> userCourse.setTokenNotification(tokenNotification));
        userCourseRepository.saveAll(userCourses);
    }


    @SneakyThrows
    @Override
    public UserCourse updateStatus(String id, ChangeStatusReq req) {
        Optional<UserCourse> rs = userCourseRepository.findById(id);
        if (!rs.isPresent()) {
            throw new NotFoundException("UserCourse does not exist");
        }
        UserCourse userCourse = rs.get();
        userCourse.setStatus(req.getStatus());

        userCourseRepository.save(userCourse);
        service.sendNotification("Bạn đã được thêm vào khóa học", "Bạn đã tham gia khóa học thành công: " + userCourse.getCourse().getName(), userCourse.getTokenNotification());
        return userCourse;
    }

    @Override
    public Page<UserCourse> findUserCourses(String username, String courseName, Integer page) {
        page--;
        if (page < 0) {
            page = 0;
        }
        Pageable pageable = PageRequest.of(page, Contant.LIMIT_USERCOURSE, Sort.by("enrollDate").descending());
        return userCourseRepository.findUserCourses(username, courseName, pageable);
    }

    @Override
    public List<CourseDTO> getUserPendingCourses(String userId) {
        List<UserCourse> userCourses = userCourseRepository.findByUser_IdAndStatus(userId, 0);

        List<CourseDTO> pendingCourseDTOs = new ArrayList<>();
        for (UserCourse userCourse : userCourses) {
            if (userCourse.getStatus() == 0) {
                CourseDTO courseDTO = new CourseDTO();
                courseDTO.setId(userCourse.getCourse().getId());
                courseDTO.setName(userCourse.getCourse().getName());
                courseDTO.setDescription(userCourse.getCourse().getDescription());
                courseDTO.setActive(userCourse.getCourse().getActive());
                courseDTO.setTotalTime(userCourse.getCourse().getTotalTime());
                courseDTO.setImage(userCourse.getCourse().getImage());
                courseDTO.setLevel(userCourse.getCourse().getLevel());
                courseDTO.setPrice(userCourse.getCourse().getPrice());
                courseDTO.setPublishedAt(userCourse.getCourse().getPublishedAt());
                courseDTO.setStatus(userCourse.getCourse().getStatus());
                courseDTO.setNumberOfSessions(userCourse.getCourse().getNumberOfSessions());
                List<String> teacherNames = new ArrayList<>();
                for (User user : userService.findUsersWithRoleTeacherInCourse(userCourse.getCourse().getId())) {
                    teacherNames.add(user.getName());
                }
                courseDTO.setTeacheNames(teacherNames);
                courseDTO.setScheduleList(userCourse.getCourse().getSchedules());
                pendingCourseDTOs.add(courseDTO);
            }
        }
        return pendingCourseDTOs;
    }

}
