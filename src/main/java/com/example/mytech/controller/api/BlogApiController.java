package com.example.mytech.controller.api;

import com.example.mytech.entity.Blog;
import com.example.mytech.entity.User;
import com.example.mytech.model.request.BlogReq;
import com.example.mytech.security.CustomUserDetails;
import com.example.mytech.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
public class BlogApiController {

    @Autowired
    private BlogService blogService ;

    @GetMapping("/admin/blog/list")
    public ResponseEntity<?> getListBlogAll () {
        List<Blog> blogs = blogService.getListBlog() ;
        return ResponseEntity.ok(blogs);
    }

    @PostMapping("/api/admin/blogs")
    public ResponseEntity<?> createBlog(@Valid @RequestBody BlogReq req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userDetails.getUser();
            Blog blog = blogService.createBlog(user,req);
            return ResponseEntity.ok(blog.getId()) ;
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized"); // Phản hồi 401 Unauthorized
        }
    }

    @PutMapping("/api/admin/update/{id}")
    public ResponseEntity<?> updatePost(@Valid @RequestBody BlogReq req, @PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userDetails.getUser();
            blogService.updateBlog(req, user, id);
            return ResponseEntity.ok("Cập nhật thành công");
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized"); // Phản hồi 401 Unauthorized
        }
    }
    @DeleteMapping("/api/admin/blogs/{id}")
    public ResponseEntity<?> deleteBlog( @PathVariable String id) {
        blogService.deletePost(id);
        return ResponseEntity.ok("Xóa thành công");
    }
}
