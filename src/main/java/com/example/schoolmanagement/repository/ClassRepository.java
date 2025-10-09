/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.example.schoolmanagement.repository;

import com.example.schoolmanagement.entity.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClassRepository extends JpaRepository<ClassEntity, Integer> {
    List<ClassEntity> findBySchoolId(Integer schoolId);
    List<ClassEntity> findByHomeroomTeacherId(Integer teacherId);
}