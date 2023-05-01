package com.example.mytech.service.impl;

import com.example.mytech.config.Contant;
import com.example.mytech.entity.User;
import com.example.mytech.entity.UserCourse;
import com.example.mytech.exception.NotFoundException;
import com.example.mytech.model.dto.UserCourseDTO;
import com.example.mytech.model.request.ChangeStatusReq;
import com.example.mytech.notification.NotificationService;
import com.example.mytech.repository.UserCourseRepository;
import com.example.mytech.service.UserCourseService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
public class UserCourseServiceImpl implements UserCourseService {

    @Autowired
    private UserCourseRepository userCourseRepository ;

    @Autowired
    private NotificationService service;

    @Override
    public List<UserCourseDTO> findByUserId(String id) {
        List<UserCourseDTO> dtos = new ArrayList<>();
        List<UserCourse> userCourses = userCourseRepository.findByUser_Id(id);
        for(UserCourse userCourse : userCourses) {
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
        service.sendNotification("Bạn đã được thêm vào khóa học","Bạn đã tham gia khóa học thành công: " + userCourse.getCourse().getName(), userCourse.getTokenNotification());
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
}
