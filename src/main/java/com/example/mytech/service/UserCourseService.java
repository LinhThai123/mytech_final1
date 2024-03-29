package com.example.mytech.service;

import com.example.mytech.entity.UserCourse;
import com.example.mytech.model.dto.CourseDTO;
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

    // update token
    void updateTokenNotification(String userId, String tokenNotification);

    public UserCourse updateStatus (String id, ChangeStatusReq req);

    public Page<UserCourse> findUserCourses (String username , String courseName , Integer page ) ;

    public List<CourseDTO> getUserPendingCourses(String userId) ;

    public List<String> getTeacherNames(String courseId);
}
