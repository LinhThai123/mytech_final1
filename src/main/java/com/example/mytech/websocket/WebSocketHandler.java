package com.example.mytech.websocket;

import com.example.mytech.entity.Blog;
import com.example.mytech.entity.Course;
import com.example.mytech.model.dto.BlogDTO;
import com.example.mytech.model.dto.CourseDTO;
import com.example.mytech.service.BlogService;
import com.example.mytech.service.CourseService;
import com.example.mytech.service.UserCourseService;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private CourseService courseService;

    @Autowired
    private BlogService blogService ;

    public static List<WebSocketSession> sessions = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Không xử lý message từ client
        log.info("hihi" + message);

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        sessions.remove(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }

    @SneakyThrows
    public void notifyCourseChange() {
        List<Course> courses = courseService.getListCourse();
        List<CourseDTO> courseDTOs = courses.stream()
                .map(course -> {

                    CourseDTO courseDTO = new CourseDTO();
                    // Chuyển đổi các trường từ course sang courseDTO
                    courseDTO.setId(course.getId());
                    courseDTO.setName(course.getName());
                    courseDTO.setDescription(course.getDescription());
                    courseDTO.setStatus(course.getStatus());
                    courseDTO.setActive(course.getActive());
                    courseDTO.setPrice(course.getPrice());
                    courseDTO.setLevel(course.getLevel());
                    courseDTO.setImage(course.getImage());
                    courseDTO.setPublishedAt(course.getPublishedAt());
                    courseDTO.setTotalTime(course.getTotalTime());
                    courseDTO.setActive(course.getActive());

                    // Các trường khác
                    return courseDTO;
                })
                .collect(Collectors.toList());

        for (WebSocketSession session : sessions) {
            System.out.println(session.getId());
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(new Gson().toJson(courseDTOs)));
                log.info(" có session nào đang mở");
            } else {
                log.info("Không có session nào đang mở");
            }
        }
    }

    @SneakyThrows
    public void notifyBlogChange () {
        List<Blog> blogs = blogService.getListBlog();
        List<BlogDTO> blogDTOS = blogs.stream()
                .map(blog -> {
                    BlogDTO blogDTO = new BlogDTO() ;
                    blogDTO.setId(blog.getId());
                    blogDTO.setTitle(blog.getTitle());
                    blogDTO.setContent(blog.getContent());
                    blogDTO.setDescription(blog.getDescription());
                    blogDTO.setImage(blog.getImage());
                    blogDTO.setStatus(blog.getStatus());
                    blogDTO.setPublishedAt(blog.getPublishedAt());
                    blogDTO.setCreateBy(blog.getCreatedBy().getName());
                    blogDTO.setModifiedAt(blog.getModifiedAt());
                    blogDTO.setModifiedBY(blog.getModifiedBy().getName());
                    return blogDTO ;
                }).collect(Collectors.toList());
        for (WebSocketSession session : sessions) {
            if(session.isOpen()) {
                session.sendMessage(new TextMessage(new Gson().toJson(blogDTOS)));
                log.info("Có session đang mở 2 ");
            }
            else {
                log.info("không có session đang mở 2 ");
            }
        }
    }
}