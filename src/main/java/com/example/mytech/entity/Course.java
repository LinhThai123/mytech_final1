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
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate expiredAt;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @JsonBackReference
    private Category category;

    @ManyToMany(mappedBy = "courses")
    @JsonIgnore
    private Set<User> users = new HashSet<>();

}
