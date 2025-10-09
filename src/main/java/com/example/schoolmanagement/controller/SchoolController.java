package com.example.schoolmanagement.controller;

import com.example.schoolmanagement.entity.School;
import com.example.schoolmanagement.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/schools")
@CrossOrigin(origins = "*")
public class SchoolController {

    @Autowired
    private SchoolService schoolService;

    @GetMapping
    public ResponseEntity<?> getSchools() {
        try {
            List<School> schools = schoolService.getAllSchools();
            return ResponseEntity.ok(Map.of("schools", schools));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to fetch schools"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSchool(@PathVariable Integer id) {
        try {
            School school = schoolService.getSchoolById(id);
            if (school == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(school);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to fetch school"));
        }
    }

    @PostMapping
    public ResponseEntity<?> createSchool(@RequestBody School school) {
        try {
            // Validation: Check if code already exists
            if (school.getCode() != null && !school.getCode().isEmpty()) {
                // Add validation logic here if needed
            }
            
            // Set default status if not provided
            if (school.getStatus() == null || school.getStatus().isEmpty()) {
                school.setStatus("ACTIVE");
            }
            
            School savedSchool = schoolService.saveSchool(school);
            
            // Logging: Log school creation
            System.out.println("School created: " + savedSchool.getName() + " (ID: " + savedSchool.getId() + ")");
            
            return ResponseEntity.ok(Map.of(
                "message", "School created successfully",
                "school", savedSchool
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to create school: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSchool(@PathVariable Integer id, @RequestBody School school) {
        try {
            School existingSchool = schoolService.getSchoolById(id);
            if (existingSchool == null) {
                return ResponseEntity.notFound().build();
            }
            
            // Update fields
            if (school.getName() != null) existingSchool.setName(school.getName());
            if (school.getCode() != null) existingSchool.setCode(school.getCode());
            if (school.getAddress() != null) existingSchool.setAddress(school.getAddress());
            if (school.getPhone() != null) existingSchool.setPhone(school.getPhone());
            if (school.getEmail() != null) existingSchool.setEmail(school.getEmail());
            if (school.getStatus() != null) existingSchool.setStatus(school.getStatus());
            
            School updatedSchool = schoolService.saveSchool(existingSchool);
            
            // Logging: Log school update
            System.out.println("School updated: " + updatedSchool.getName() + " (ID: " + updatedSchool.getId() + ")");
            
            return ResponseEntity.ok(Map.of(
                "message", "School updated successfully",
                "school", updatedSchool
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to update school: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSchool(@PathVariable Integer id) {
        try {
            School school = schoolService.getSchoolById(id);
            if (school == null) {
                return ResponseEntity.notFound().build();
            }
            
            schoolService.deleteSchool(id);
            
            // Logging: Log school deletion
            System.out.println("School deleted: " + school.getName() + " (ID: " + school.getId() + ")");
            
            return ResponseEntity.ok(Map.of("message", "School deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to delete school: " + e.getMessage()));
        }
    }
}

