package com.example.mytech.repository;

import com.example.mytech.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, String> {

    Page<Schedule> findScheduleByIdOrCourse_NameContaining(String id, String nameCourse, Pageable pageable);
}
