package com.example.mytech.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "course")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Course {
    @GenericGenerator(name = "random_id", strategy = "com.example.mytech.model.custom.RandomIdGenerator")
    @Id
    @GeneratedValue(generator = "random_id")
    private String id;

    @Column(name = "name" , nullable = false)
    private String name ;

    @Column(name = "slug")
    private String slug;

    @Column(name = "description" , columnDefinition = "TEXT")
    private String description;

    @Column(name = "status" , nullable = false , columnDefinition = "TINYINT(1)")
    private int status;

    @Column(name = "active" ,columnDefinition = "TINYINT(1)")
    private int active;

    @Column (name = "price" , nullable = false)
    private long price ;

    @Column(name = "is_level" , nullable = false)
    private int level;

    @Column(name = "image")
    private String image;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "modified_at", nullable = false)
    private Timestamp modifiedAt;

    @Column (name = "published_at")
    private Timestamp publishedAt;

    @Column(name = "expired_at")
    private Date expiredAt;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @JsonBackReference
    private Category category;

    @ManyToMany(mappedBy = "courses")
    @JsonIgnore
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Schedule> schedules;

    @PreRemove
    public void remove() {
        for (User user : users) {
            user.getCourses().remove(this);
        }
    }
    public LocalDate getStartDate() {
        return createdAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public LocalDate getEndDate() {
        if (expiredAt == null) {
            return null;
        }
        return expiredAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public List<Schedule> generateWeeklySchedules(int numberOfWeeks) {
        List<Schedule> weeklySchedules = new ArrayList<>();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        for (Schedule schedule : this.schedules) {
            LocalTime startTime = LocalTime.parse(schedule.getStartTime(), timeFormatter);
            LocalTime endTime = LocalTime.parse(schedule.getEndTime(), timeFormatter);

            LocalDate courseStartDate = this.getStartDate();
            LocalDate courseEndDate = this.getEndDate();

            // Kiểm tra số tuần được chỉ định có nằm trong khoảng thời gian của khóa học không
            LocalDate startDateForWeeks = courseStartDate.plusWeeks(numberOfWeeks - 1);
            if (startDateForWeeks.isAfter(courseEndDate)) {
                continue;
            }

            for (int i = 0; i < numberOfWeeks; i++) {
                LocalDateTime newStartDateTime = LocalDateTime.of(courseStartDate.plusWeeks(i), startTime);
                LocalDateTime newEndDateTime = LocalDateTime.of(courseStartDate.plusWeeks(i), endTime);

                Schedule weeklySchedule = new Schedule();
                weeklySchedule.setDayOfWeek(schedule.getDayOfWeek());
                weeklySchedule.setStartTime(newStartDateTime.toString());
                weeklySchedule.setEndTime(newEndDateTime.toString());

                weeklySchedules.add(weeklySchedule);
            }
        }

        return weeklySchedules;
    }
}
