package com.example.schoolmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "classes")
@Data
public class ClassEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;

    private String name;

    private Integer gradeLevel;

    private String schoolYear;

    @ManyToOne
    @JoinColumn(name = "homeroom_teacher_id")
    private User homeroomTeacher;

    private Integer capacity;

    private String status;
}