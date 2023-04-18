package com.example.mytech.repository;

import com.example.mytech.entity.Attendance;
import com.example.mytech.entity.Schedule;
import com.example.mytech.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance , String> {

    Attendance findByUserAndSchedule(User student, Schedule schedule);

    @Query("SELECT a FROM Attendance a WHERE a.schedule.id = :scheduleId")
    List<Attendance> getAttendanceListByScheduleId(@Param("scheduleId") String scheduleId);

}
