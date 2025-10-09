package com.example.schoolmanagement.service;

import com.example.schoolmanagement.entity.ClassEntity;
import com.example.schoolmanagement.repository.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassService {
    @Autowired
    private ClassRepository classRepository;

    public List<ClassEntity> getAllClasses() {
        return classRepository.findAll();
    }

    public ClassEntity getClassById(Integer id) {
        return classRepository.findById(id).orElse(null);
    }

    public List<ClassEntity> getClassesBySchool(Integer schoolId) {
        return classRepository.findBySchoolId(schoolId);
    }

    public List<ClassEntity> getClassesByHomeroomTeacher(Integer teacherId) {
        return classRepository.findByHomeroomTeacherId(teacherId);
    }

    public ClassEntity saveClass(ClassEntity classEntity) {
        return classRepository.save(classEntity);
    }

    public void deleteClass(Integer id) {
        classRepository.deleteById(id);
    }
}



