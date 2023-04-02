package com.example.mytech.service;

import com.example.mytech.entity.User;
import com.example.mytech.entity.UserCourse;
import com.example.mytech.model.request.ChangeStatusReq;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface UserCourseService {

    // find by id
    public UserCourse getUserCourseById (String id) ;

    public UserCourse updateStatus (String id, ChangeStatusReq req);

    public Page<UserCourse> findUserCourses (String username , String courseName , Integer page ) ;
}
