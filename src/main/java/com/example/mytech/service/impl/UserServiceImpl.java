package com.example.mytech.service.impl;

import com.example.mytech.config.Contant;
import com.example.mytech.entity.*;
import com.example.mytech.exception.BadRequestException;
import com.example.mytech.exception.InternalServerException;
import com.example.mytech.exception.NotFoundException;
import com.example.mytech.model.request.RegisterRep;
import com.example.mytech.model.request.UserRep;
import com.example.mytech.repository.CourseRepository;
import com.example.mytech.repository.RoleRepository;
import com.example.mytech.repository.UserCourseRepository;
import com.example.mytech.repository.UserRepository;
import com.example.mytech.service.ImageService;
import com.example.mytech.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserCourseRepository userCourseRepository ;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private ImageService imageService;

    @Autowired
    private CourseRepository courseRepository ;

    @Override
    public List<User> getListUser() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(UserRep rep) {
        User user = new User();

        user.setName(rep.getName());

        user.setPassword(encoder.encode(rep.getPassword()));

        // check trung email
        if (userRepository.existsByEmail(rep.getEmail())) {
            throw new BadRequestException("Email đã có trong hệ thống , Vui lòng chọn email khác");
        }
        user.setEmail(rep.getEmail());

        user.setPhone(rep.getPhone());

        user.setGender(rep.getGender());

        user.setImage(rep.getImage());

        user.setAddress(rep.getAddress());

        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        String dateString = String.valueOf(rep.getDateOfBirth());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(dateString, formatter);
        user.setDateOfBirth(date.plusDays(1));

        user.setStatus(true);

        List<String> strRoles = rep.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error : Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error : Role is not found"));
                        roles.add(adminRole);
                        break;

                    case "teacher":
                        Role teacherRole = roleRepository.findByName(ERole.ROLE_TEACHER)
                                .orElseThrow(() -> new RuntimeException("Error : Role is not found"));
                        roles.add(teacherRole);
                        break;
                    default:
                        Role usersRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error : Role is not found"));
                        roles.add(usersRole);
                }
            });
        }
        user.setRoles(roles);
        try {
            userRepository.save(user);
            return user;
        } catch (Exception e) {
            throw new InternalServerException("Lỗi khi thêm học viên ");
        }
    }

    @Override
    public User registerUser(RegisterRep rep) {

        User user = new User();

        user.setName(rep.getName());

        user.setPassword(encoder.encode(rep.getPassword()));

        // check trung email
        if (userRepository.existsByEmail(rep.getEmail())) {
            throw new BadRequestException("Email đã có trong hệ thống , Vui lòng chọn email khác");
        }
        user.setEmail(rep.getEmail());;

        List<String> strRoles = rep.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error : Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error : Role is not found"));
                        roles.add(adminRole);
                        break;

                    case "teacher":
                        Role teacherRole = roleRepository.findByName(ERole.ROLE_TEACHER)
                                .orElseThrow(() -> new RuntimeException("Error : Role is not found"));
                        roles.add(teacherRole);
                        break;
                    default:
                        Role usersRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error : Role is not found"));
                        roles.add(usersRole);
                }
            });
        }
        user.setRoles(roles);
        try {
            userRepository.save(user);
            return user;
        } catch (Exception e) {
            throw new InternalServerException("Lỗi khi đăng ký học viên ");
        }
    }

    @Override
    public Page<User> findUserByNameOrId(String id, String nameTeacher, Integer page) {
        page--;
        if (page < 0) {
            page = 0;
        }
        Pageable pageable = PageRequest.of(page, Contant.LIMIT_USER, Sort.by("createdAt").descending());
        return userRepository.findUserByNameContainingOrId(id, nameTeacher,pageable);
    }

    @Override
    public List<User> getUserWithRoleUser() {
        return userRepository.findUsersWithUserRole();
    }
    @Override
    public User updateUser(String id, UserRep rep) {
        User user;
        Optional<User> rs = userRepository.findById(id);
        user = rs.get();
        if (!rs.isPresent()) {
            throw new NotFoundException("User do not exits");
        }
        user.setName(rep.getName());

        user.setEmail(rep.getEmail());

        user.setGender(rep.getGender());

        user.setPhone(rep.getPhone());

        user.setImage(rep.getImage());

        String dateString = String.valueOf(rep.getDateOfBirth());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(dateString, formatter);
        user.setDateOfBirth(date.plusDays(1));

        user.setAddress(rep.getAddress());

        user.setModifiedAt(new Timestamp(System.currentTimeMillis()));

        user.setStatus(true);

        List<String> strRoles = rep.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error : Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error : Role is not found"));
                        roles.add(adminRole);
                        break;

                    case "teacher":
                        Role teacherRole = roleRepository.findByName(ERole.ROLE_TEACHER)
                                .orElseThrow(() -> new RuntimeException("Error : Role is not found"));
                        roles.add(teacherRole);
                        break;
                    default:
                        Role usersRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error : Role is not found"));
                        roles.add(usersRole);
                }
            });
        }
        user.setRoles(roles);
        try {
            userRepository.save(user);
            return user;
        } catch (Exception e) {
            throw new InternalServerException("Lỗi khi cập nhật thông tin user ");
        }
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }
    @Override
    public User getUserById(String id) {
        Optional<User> rs = userRepository.findById(id);
        if (!rs.isPresent()) {
            throw new NotFoundException("User do not exits");
        }
        return rs.get();
    }

    @Override
    public String uploadFile(MultipartFile file) {
        return imageService.uploadFile(file);
    }

    @Override
    public List<User> findUsersWithRoleUserInCourse(String courseId) {
        return userRepository.findUsersWithRoleUserInCourse(courseId);
    }

    @Override
    public List<User> findUsersWithRoleTeacherInCourse(String courseId) {
        return userRepository.findUsersWithRoleTeacherInCourse(courseId);
    }

    @Override
    public List<Course> enrollCourse(String userId, String courseId) {
        // Lấy thông tin học viên
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy học viên với ID = " + userId));

        // Kiểm tra xem học viên có quyền role_user hay không
        if (user.getRoles().equals(ERole.ROLE_USER.label)) {
            throw new AccessDeniedException("Bạn không có quyền đăng ký khóa học.");
        }
        // Lấy thông tin khóa học
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy khóa học với ID = " + courseId));
        // Lấy danh sách các khóa học đã đăng ký của người dùng
        List<Course> userCourses = userCourseRepository.findByUser(user).stream()
                .map(UserCourse::getCourse)
                .collect(Collectors.toList());

        // Kiểm tra xem khóa học đã được đăng ký chưa
        if (userCourses.contains(course)) {
            throw new AccessDeniedException("Bạn đã đăng ký vào khóa học này rồi.");
        }
        try{
            UserCourse userCourse = new UserCourse();
            userCourse.setCourse(course);
            userCourse.setUser(user);
            userCourse.setEnrollDate(LocalDateTime.now());
            userCourseRepository.save(userCourse);
        }catch (Exception e){
            throw new InternalServerException("Lỗi khi thêm khóa học") ;
        }
        return userCourses;
    }

    @Override
    public byte[] readFile(String fileId) {
        return imageService.readFile(fileId);
    }

}
