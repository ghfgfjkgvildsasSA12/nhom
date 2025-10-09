package com.example.schoolmanagement.controller;

import com.example.schoolmanagement.entity.ClassEntity;
import com.example.schoolmanagement.entity.School;
import com.example.schoolmanagement.entity.User;
import com.example.schoolmanagement.service.ClassService;
import com.example.schoolmanagement.repository.SchoolRepository;
import com.example.schoolmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/classes")
@CrossOrigin(origins = "*")
public class ClassController {

    @Autowired
    private ClassService classService;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<?> getClasses() {
        try {
            List<ClassEntity> classes = classService.getAllClasses();
            return ResponseEntity.ok(Map.of("classes", classes));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to fetch classes"));
        }
    }

    @GetMapping("/school/{schoolId}")
    public ResponseEntity<?> getClassesBySchool(@PathVariable Integer schoolId) {
        try {
            List<ClassEntity> classes = classService.getClassesBySchool(schoolId);
            return ResponseEntity.ok(Map.of("classes", classes));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to fetch classes by school"));
        }
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<?> getClassesByTeacher(@PathVariable Integer teacherId) {
        try {
            List<ClassEntity> classes = classService.getClassesByHomeroomTeacher(teacherId);
            return ResponseEntity.ok(Map.of("classes", classes));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to fetch classes by teacher"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClass(@PathVariable Integer id) {
        try {
            ClassEntity classEntity = classService.getClassById(id);
            if (classEntity == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(classEntity);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to fetch class"));
        }
    }

    @PostMapping
    public ResponseEntity<?> createClass(@RequestBody Map<String, Object> classData) {
        try {
            ClassEntity classEntity = new ClassEntity();
            
            // Set basic fields
            classEntity.setName((String) classData.get("name"));
            classEntity.setGradeLevel((Integer) classData.get("gradeLevel"));
            classEntity.setSchoolYear((String) classData.get("schoolYear"));
            classEntity.setCapacity((Integer) classData.get("capacity"));
            classEntity.setStatus((String) classData.getOrDefault("status", "ACTIVE"));
            
            // Set school
            Integer schoolId = (Integer) classData.get("schoolId");
            if (schoolId != null) {
                Optional<School> school = schoolRepository.findById(schoolId);
                if (school.isPresent()) {
                    classEntity.setSchool(school.get());
                } else {
                    return ResponseEntity.badRequest().body(Map.of("error", "Invalid school ID"));
                }
            }
            
            // Set homeroom teacher
            Integer teacherId = (Integer) classData.get("homeroomTeacherId");
            if (teacherId != null) {
                Optional<User> teacher = userRepository.findById(teacherId);
                if (teacher.isPresent()) {
                    classEntity.setHomeroomTeacher(teacher.get());
                } else {
                    return ResponseEntity.badRequest().body(Map.of("error", "Invalid teacher ID"));
                }
            }
            
            // Validation: Check capacity
            if (classEntity.getCapacity() != null && classEntity.getCapacity() <= 0) {
                return ResponseEntity.badRequest().body(Map.of("error", "Capacity must be greater than 0"));
            }
            
            // Validation: Check status
            if (classEntity.getStatus() == null || classEntity.getStatus().trim().isEmpty()) {
                classEntity.setStatus("ACTIVE");
            }
            
            ClassEntity savedClass = classService.saveClass(classEntity);
            
            // Logging: Log class creation
            System.out.println("Class created: " + savedClass.getName() + " (ID: " + savedClass.getId() + ")");
            
            return ResponseEntity.ok(Map.of(
                "message", "Class created successfully",
                "class", savedClass
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to create class: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateClass(@PathVariable Integer id, @RequestBody Map<String, Object> classData) {
        try {
            ClassEntity existingClass = classService.getClassById(id);
            if (existingClass == null) {
                return ResponseEntity.notFound().build();
            }
            
            // Update fields
            if (classData.get("name") != null) {
                existingClass.setName((String) classData.get("name"));
            }
            
            if (classData.get("gradeLevel") != null) {
                existingClass.setGradeLevel((Integer) classData.get("gradeLevel"));
            }
            
            if (classData.get("schoolYear") != null) {
                existingClass.setSchoolYear((String) classData.get("schoolYear"));
            }
            
            if (classData.get("capacity") != null) {
                Integer capacity = (Integer) classData.get("capacity");
                if (capacity <= 0) {
                    return ResponseEntity.badRequest().body(Map.of("error", "Capacity must be greater than 0"));
                }
                existingClass.setCapacity(capacity);
            }
            
            if (classData.get("status") != null) {
                existingClass.setStatus((String) classData.get("status"));
            }
            
            // Update school
            if (classData.get("schoolId") != null) {
                Integer schoolId = (Integer) classData.get("schoolId");
                Optional<School> school = schoolRepository.findById(schoolId);
                if (school.isPresent()) {
                    existingClass.setSchool(school.get());
                } else {
                    return ResponseEntity.badRequest().body(Map.of("error", "Invalid school ID"));
                }
            }
            
            // Update homeroom teacher
            if (classData.get("homeroomTeacherId") != null) {
                Integer teacherId = (Integer) classData.get("homeroomTeacherId");
                Optional<User> teacher = userRepository.findById(teacherId);
                if (teacher.isPresent()) {
                    existingClass.setHomeroomTeacher(teacher.get());
                } else {
                    return ResponseEntity.badRequest().body(Map.of("error", "Invalid teacher ID"));
                }
            }
            
            ClassEntity updatedClass = classService.saveClass(existingClass);
            
            // Logging: Log class update
            System.out.println("Class updated: " + updatedClass.getName() + " (ID: " + updatedClass.getId() + ")");
            
            return ResponseEntity.ok(Map.of(
                "message", "Class updated successfully",
                "class", updatedClass
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to update class: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClass(@PathVariable Integer id) {
        try {
            ClassEntity classEntity = classService.getClassById(id);
            if (classEntity == null) {
                return ResponseEntity.notFound().build();
            }
            
            classService.deleteClass(id);
            
            // Logging: Log class deletion
            System.out.println("Class deleted: " + classEntity.getName() + " (ID: " + classEntity.getId() + ")");
            
            return ResponseEntity.ok(Map.of("message", "Class deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to delete class: " + e.getMessage()));
        }
    }
}



