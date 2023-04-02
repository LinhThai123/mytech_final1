package com.example.mytech.service.impl;

import com.example.mytech.config.Contant;
import com.example.mytech.entity.User;
import com.example.mytech.entity.UserCourse;
import com.example.mytech.exception.NotFoundException;
import com.example.mytech.model.request.ChangeStatusReq;
import com.example.mytech.repository.UserCourseRepository;
import com.example.mytech.service.UserCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;


@Component
public class UserCourseServiceImpl implements UserCourseService {

    @Autowired
    private UserCourseRepository userCourseRepository ;

    @Override
    public UserCourse getUserCourseById(String id) {
        Optional<UserCourse> rs = userCourseRepository.findById(id);
        if (!rs.isPresent()) {
            throw new NotFoundException("UserCourse do not exits");
        }
        return rs.get();
    }

    @Override
    public UserCourse updateStatus(String id, ChangeStatusReq req) {
        Optional<UserCourse> rs = userCourseRepository.findById(id);
        if (!rs.isPresent()) {
            throw new NotFoundException("UserCourse does not exist");
        }
        UserCourse userCourse = rs.get();
        userCourse.setStatus(req.getStatus());
        userCourseRepository.save(userCourse);
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
