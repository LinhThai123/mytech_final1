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

    @Query("SELECT new com.example.mytech.model.dto.AttendanceDTO(a.id , a.user.id, a.attendance) " +
            "FROM Attendance a WHERE a.schedule.id = :scheduleId")
    List<AttendanceDTO> getUserAndAttendanceByScheduleId(@Param("scheduleId") String scheduleId);

    Attendance findByUserId(String userId);

    List<Attendance> findByUserIdIn(List<String> userIds);

    List<Attendance> findByAttendanceIsTrue();

    List<Attendance> findByAttendanceIsFalse();

}
