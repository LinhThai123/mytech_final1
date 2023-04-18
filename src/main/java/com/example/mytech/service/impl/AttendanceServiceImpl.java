package com.example.mytech.service.impl;

import com.example.mytech.entity.*;
import com.example.mytech.exception.NotFoundException;
import com.example.mytech.model.dto.AttendanceDTO;
import com.example.mytech.model.dto.AttendanceResponseDTO;
import com.example.mytech.model.dto.CourseResponseDTO;
import com.example.mytech.model.dto.ScheduleResponseDTO;
import com.example.mytech.repository.AttendanceRepository;
import com.example.mytech.repository.CourseRepository;
import com.example.mytech.repository.ScheduleRepository;
import com.example.mytech.repository.UserRepository;
import com.example.mytech.service.AttendanceService;
import com.example.mytech.service.CourseService;
import com.example.mytech.service.UserService;
import com.example.mytech.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScheduleRepository scheduleRepository ;

    @Autowired
    private AttendanceRepository attendanceRepository ;

    @Autowired
    private CourseRepository courseRepository ;

    @Autowired
    private UserService userService ;

    @Override
    public List<AttendanceDTO> getAttendanceListForCourse(String courseId) {
        return attendanceRepository.findAttendanceListForCourse(courseId);
    }

    @Override
    public void markAttendance(List<AttendanceDTO> attendanceList) {
        // Tạo danh sách các đối tượng Attendance cần lưu
        List<Attendance> attendancesToSave = new ArrayList<>();

        for (AttendanceDTO attendanceDTO : attendanceList) {
            // Kiểm tra xem học viên có tồn tại hay không
            User student = userRepository.findById(attendanceDTO.getUserID())
                    .orElseThrow(() -> new NotFoundException("Học viên không tồn tại"));

            // Kiểm tra xem lịch học có tồn tại hay không
            Schedule schedule = scheduleRepository.findById(attendanceDTO.getScheduleID())
                    .orElseThrow(() -> new NotFoundException("Lịch học không tồn tại"));

            // Kiểm tra danh sách học viên phải là danh sách học viên của khóa học
            List<User> usersInCourse = userRepository.findUsersWithRoleUserInCourse(schedule.getCourse().getId());
            if (!usersInCourse.stream().map(User::getId).collect(Collectors.toList()).contains(student.getId())) {
                throw new IllegalArgumentException("Học viên : " + student.getName() + " không thuộc khóa học : " + schedule.getCourse().getName());
            }

            // Cập nhật trạng thái điểm danh của học viên trong lịch học
            Attendance attendance = attendanceRepository.findByUserAndSchedule(student, schedule);
            if (attendance == null) {
                // Nếu chưa có bản ghi điểm danh, tạo mới và thêm vào danh sách cần lưu
                attendance = new Attendance();
                attendance.setUser(student);
                attendance.setSchedule(schedule);
                attendance.setAttendance(false);
                attendancesToSave.add(attendance);
            }
        }
        // Lưu danh sách các đối tượng Attendance vào CSDL
        attendanceRepository.saveAll(attendancesToSave);
    }

    @Override
    public CourseResponseDTO getAttendanceListByCourseId(String courseId) {
//        // Lấy danh sách học viên của khóa học từ userService
//        List<User> userIdList = userService.findUsersWithRoleUserInCourse(courseId);
//        // Lấy thông tin khóa học từ courseRepository
//        Course course = courseRepository.findById(courseId)
//                .orElseThrow(() -> new NotFoundException("Course does not exist"));
//
//        // Tạo đối tượng CourseDTO để lưu thông tin khóa học
//        CourseResponseDTO dto = new CourseResponseDTO();
////        dto.setCourseId(courseId);
//        List<AttendanceResponseDTO> attendanceResponseList = new ArrayList<>();
//
//        // Lấy danh sách schedules từ đối tượng Course
//        List<Schedule> schedules = course.getSchedules();
//
//        for (User user : userIdList) {
//            // Lấy thông tin học viên từ userId
//            User userInfo = userRepository.findById(user.getId())
//                    .orElseThrow(() -> new NotFoundException("User does not exist"));
//
//            // Tạo đối tượng AttendanceResponseDTO từ thông tin học viên
//            AttendanceResponseDTO attendanceResponse = new AttendanceResponseDTO();
//            attendanceResponse.setUser(userInfo);
//
//            // Kiểm tra xem schedules có phần tử hay không
//            if (!schedules.isEmpty()) {
//                // Lấy ID của Schedule từ danh sách schedules
//                Schedule schedule = schedules.get(0); // Ví dụ: lấy phần tử đầu tiên
//                attendanceResponse.setSchedule(schedule);
//            }
//
//            // Thêm đối tượng AttendanceResponseDTO vào danh sách điểm danh
//            attendanceResponseList.add(attendanceResponse);
//        }
//
//        // Lưu danh sách điểm danh vào CourseDTO
//        dto.setAttendanceResponseList(attendanceResponseList);
//
//        return dto;
        return null;
    }
//TODO chưa xong
    @Override
    public ScheduleResponseDTO getUserOfCourseByScheduleId (String scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElse(null);
        if (schedule == null) {
            // Xử lý lỗi nếu không tìm thấy Schedule
            throw new RuntimeException("Không tìm thấy lịch học");
        }

        ScheduleResponseDTO scheduleResponseDTO = new ScheduleResponseDTO();
        scheduleResponseDTO.setScheduleId(schedule.getId());

        List<AttendanceResponseDTO> attendanceResponseList = new ArrayList<>();

        List<User> users = userRepository.getUsersByScheduleId(schedule.getId()) ;
        for (User user : users) {
            AttendanceResponseDTO attendanceResponseDTO = new AttendanceResponseDTO();
            attendanceResponseDTO.setUser(user);
            attendanceResponseList.add(attendanceResponseDTO);
        }
        scheduleResponseDTO.setAttendanceResponseList(attendanceResponseList);
        return scheduleResponseDTO;
    }

    @Override
    public List<User> getUsersByScheduleId(String scheduleId) {
        return userRepository.getUsersByScheduleId(scheduleId);
    }
}
