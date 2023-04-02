package com.example.mytech.service;

import com.example.mytech.entity.UserCourse;
import com.example.mytech.model.request.ChangeStatusReq;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface UserCourseService {

    public void updateStatus (String id, ChangeStatusReq req);

    public Page<UserCourse> findUserCourses (String username , String courseName , Integer page ) ;
}
