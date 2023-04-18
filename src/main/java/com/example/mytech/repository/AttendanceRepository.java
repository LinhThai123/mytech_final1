package com.example.mytech.repository;

import com.example.mytech.entity.Attendance;
import com.example.mytech.entity.Schedule;
import com.example.mytech.entity.User;
import com.example.mytech.model.dto.AttendanceDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance , String> {

    Attendance findByUserAndSchedule(User student, Schedule schedule);

    @Query("SELECT new com.example.mytech.model.dto.AttendanceDTO(u.name, s.id, a.attendance) " +
            "FROM User u " +
            "JOIN Attendance a ON u.id = a.user.id " +
            "JOIN Schedule s ON a.schedule.id = s.id " +
            "JOIN Course c ON s.course.id = c.id " +
            "WHERE c.id = :courseId")
    List<AttendanceDTO> findAttendanceListForCourse(@Param("courseId") String courseId);

}
