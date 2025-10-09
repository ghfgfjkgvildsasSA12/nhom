package com.example.schoolmanagement.controller;

import com.example.schoolmanagement.entity.Subject;
import com.example.schoolmanagement.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/subjects")
@CrossOrigin(origins = "*")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @GetMapping
    public ResponseEntity<?> getSubjects() {
        try {
            List<Subject> subjects = subjectService.getAllSubjects();
            return ResponseEntity.ok(Map.of("subjects", subjects));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to fetch subjects"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSubject(@PathVariable Integer id) {
        try {
            Subject subject = subjectService.getSubjectById(id);
            if (subject == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(subject);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to fetch subject"));
        }
    }

    @PostMapping
    public ResponseEntity<?> createSubject(@RequestBody Subject subject) {
        try {
            // Validation: Check if name and code are provided
            if (subject.getName() == null || subject.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Subject name is required"));
            }
            
            if (subject.getCode() == null || subject.getCode().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Subject code is required"));
            }
            
            Subject savedSubject = subjectService.saveSubject(subject);
            
            // Logging: Log subject creation
            System.out.println("Subject created: " + savedSubject.getName() + " (ID: " + savedSubject.getId() + ")");
            
            return ResponseEntity.ok(Map.of(
                "message", "Subject created successfully",
                "subject", savedSubject
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to create subject: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSubject(@PathVariable Integer id, @RequestBody Subject subject) {
        try {
            Subject existingSubject = subjectService.getSubjectById(id);
            if (existingSubject == null) {
                return ResponseEntity.notFound().build();
            }
            
            // Update fields
            if (subject.getName() != null) existingSubject.setName(subject.getName());
            if (subject.getCode() != null) existingSubject.setCode(subject.getCode());
            if (subject.getSchool() != null) existingSubject.setSchool(subject.getSchool());
            
            Subject updatedSubject = subjectService.saveSubject(existingSubject);
            
            // Logging: Log subject update
            System.out.println("Subject updated: " + updatedSubject.getName() + " (ID: " + updatedSubject.getId() + ")");
            
            return ResponseEntity.ok(Map.of(
                "message", "Subject updated successfully",
                "subject", updatedSubject
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to update subject: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSubject(@PathVariable Integer id) {
        try {
            Subject subject = subjectService.getSubjectById(id);
            if (subject == null) {
                return ResponseEntity.notFound().build();
            }
            
            subjectService.deleteSubject(id);
            
            // Logging: Log subject deletion
            System.out.println("Subject deleted: " + subject.getName() + " (ID: " + subject.getId() + ")");
            
            return ResponseEntity.ok(Map.of("message", "Subject deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to delete subject: " + e.getMessage()));
        }
    }
}



