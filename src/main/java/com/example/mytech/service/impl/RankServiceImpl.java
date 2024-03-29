package com.example.mytech.service.impl;


import com.example.mytech.entity.Course;
import com.example.mytech.entity.Rank;
import com.example.mytech.entity.User;
import com.example.mytech.exception.InternalServerException;
import com.example.mytech.exception.NotFoundException;
import com.example.mytech.model.dto.RankUserDTO;
import com.example.mytech.model.request.RankReq;
import com.example.mytech.repository.CourseRepository;
import com.example.mytech.repository.RankRepository;
import com.example.mytech.repository.UserRepository;
import com.example.mytech.service.RankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RankServiceImpl implements RankService {

    @Autowired
    private RankRepository rankRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public void addGrade(User user, Course course, RankReq req) {
        Rank rank = rankRepository.findByUserAndCourse(user, course);

        if (rank == null) {
            rank = new Rank();
            rank.setUser(user);
            rank.setCourse(course);
        }
        rank.setMidtermGrades(req.getMidtermGrades());
        rank.setFinalGrades(req.getFinalGrades());
        rank.setExams(req.getExams());
        rank.setAvg((req.getMidtermGrades() + req.getFinalGrades() + req.getExams()) / 3);

        if (rank.getAvg() >= 5) {
            rank.setRanking("Pass");
        } else {
            rank.setRanking("Fail");
        }
        try {
            rankRepository.save(rank);
        } catch (Exception e) {
            throw new InternalServerException("Lỗi khi thêm điểm cho học viên");
        }
    }

    @Override
    public Rank updateRank(String course_id, String user_id, RankReq req) {

        Rank existingRank = rankRepository.findByCourseIdAndUserId(course_id, user_id);
        if (existingRank == null) {
            throw new NotFoundException("Rank not found");
        }
        existingRank.setMidtermGrades(req.getMidtermGrades());
        existingRank.setFinalGrades(req.getFinalGrades());
        existingRank.setExams(req.getExams());

        existingRank.setAvg((req.getMidtermGrades() + req.getFinalGrades() + req.getExams()) / 3);

        if (existingRank.getAvg() >= 5) {
            existingRank.setRanking("Pass");
        } else {
            existingRank.setRanking("Fail");
        }
        try {
            rankRepository.save(existingRank);
        } catch (Exception e) {
            throw new InternalServerException("Lỗi khi cập nhật điểm cho học viên");
        }
        return existingRank;
    }

    @Override
    public List<Rank> getRanksByCourseId(String course_id) {
        return rankRepository.findByCourseId(course_id);
    }

    @Override
    public RankUserDTO getRankByCourseIdAndUserId(String courseId, String userId) {

        Optional<User> user = userRepository.findById(userId) ;
        if(!user.isPresent()) {
            throw new NotFoundException("Không tìm thấy học viên");
        }

        Rank rank = rankRepository.findByCourseIdAndUserId(courseId, user.get().getId());
        RankUserDTO dto = new RankUserDTO();
        dto.setName(user.get().getName());
        dto.setImage(user.get().getImage());
        dto.setMidtermGrades(rank.getMidtermGrades());
        dto.setFinalGrades(rank.getFinalGrades());
        dto.setExams(rank.getExams());
        dto.setAvg(rank.getAvg());
        dto.setRanking(rank.getRanking());
        return dto;
    }


    @Override
    public void deleteRankById(String id) {
        rankRepository.deleteById(id);
    }
}
