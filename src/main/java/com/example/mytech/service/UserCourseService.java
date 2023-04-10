package com.example.mytech.service;

import com.example.mytech.entity.User;
import com.example.mytech.entity.UserCourse;
import com.example.mytech.model.dto.UserCourseDTO;
import com.example.mytech.model.request.ChangeStatusReq;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserCourseService {

    List<UserCourseDTO> findByUserId(String id);

    // find by id
    public UserCourse getUserCourseById (String id) ;

    public UserCourse updateStatus (String id, ChangeStatusReq req);

    public Page<UserCourse> findUserCourses (String username , String courseName , Integer page ) ;
}
