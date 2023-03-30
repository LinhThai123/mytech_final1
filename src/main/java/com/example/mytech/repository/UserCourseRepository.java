package com.example.mytech.repository;

import com.example.mytech.entity.User;
import com.example.mytech.entity.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCourseRepository extends JpaRepository<UserCourse, String> {

    List<UserCourse> findByUser(User user);
}
