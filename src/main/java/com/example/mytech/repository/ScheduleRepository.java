package com.example.mytech.repository;

import com.example.mytech.entity.Schedule;
import com.example.mytech.model.dto.ScheduleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, String> {

    @Query("SELECT new com.example.mytech.model.dto.ScheduleDTO(s.id, c.name) FROM Schedule s JOIN s.course c WHERE s.id = :scheduleId AND c.name = :courseName")
    Page<ScheduleDTO> findScheduleDTOByIdAndName(@Param("scheduleId") String scheduleId, @Param("courseName") String courseName, Pageable pageable);

    @Query("SELECT new com.example.mytech.model.dto.ScheduleDTO(s.id, c.name) FROM Schedule s JOIN s.course c ")
    Page<ScheduleDTO> findScheduleByCourseName(Pageable pageable);

    List<Schedule> findByCourseId(String courseId);


}
