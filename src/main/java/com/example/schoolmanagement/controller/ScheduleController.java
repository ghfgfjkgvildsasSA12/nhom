package com.example.schoolmanagement.controller;

import com.example.schoolmanagement.entity.Schedule;
import com.example.schoolmanagement.entity.ClassEntity;
import com.example.schoolmanagement.entity.Subject;
import com.example.schoolmanagement.entity.User;
import com.example.schoolmanagement.service.ScheduleService;
import com.example.schoolmanagement.repository.ClassRepository;
import com.example.schoolmanagement.repository.SubjectRepository;
import com.example.schoolmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/schedules")
@CrossOrigin(origins = "*")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<?> getSchedules() {
        try {
            List<Schedule> schedules = scheduleService.getAllSchedules();
            return ResponseEntity.ok(Map.of("schedules", schedules));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to fetch schedules"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSchedule(@PathVariable Integer id) {
        try {
            Schedule schedule = scheduleService.getScheduleById(id);
            if (schedule == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(schedule);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to fetch schedule"));
        }
    }

    @PostMapping
    public ResponseEntity<?> createSchedule(@RequestBody Map<String, Object> scheduleData) {
        try {
            Schedule schedule = new Schedule();
            
            // Set basic fields
            schedule.setDayOfWeek((Integer) scheduleData.get("dayOfWeek"));
            schedule.setPeriod((Integer) scheduleData.get("period"));
            schedule.setRoom((String) scheduleData.get("room"));
            
            // Set class
            Integer classId = (Integer) scheduleData.get("classId");
            if (classId != null) {
                Optional<ClassEntity> classEntity = classRepository.findById(classId);
                if (classEntity.isPresent()) {
                    schedule.setClassEntity(classEntity.get());
                } else {
                    return ResponseEntity.badRequest().body(Map.of("error", "Invalid class ID"));
                }
            }
            
            // Set subject
            Integer subjectId = (Integer) scheduleData.get("subjectId");
            if (subjectId != null) {
                Optional<Subject> subject = subjectRepository.findById(subjectId);
                if (subject.isPresent()) {
                    schedule.setSubject(subject.get());
                } else {
                    return ResponseEntity.badRequest().body(Map.of("error", "Invalid subject ID"));
                }
            }
            
            // Set teacher
            Integer teacherId = (Integer) scheduleData.get("teacherId");
            if (teacherId != null) {
                Optional<User> teacher = userRepository.findById(teacherId);
                if (teacher.isPresent()) {
                    schedule.setTeacher(teacher.get());
                } else {
                    return ResponseEntity.badRequest().body(Map.of("error", "Invalid teacher ID"));
                }
            }
            
            // Validation: Check for time conflicts
            // This would need to be implemented in the service layer
            // For now, we'll just save the schedule
            
            Schedule savedSchedule = scheduleService.saveSchedule(schedule);
            
            // Logging: Log schedule creation
            System.out.println("Schedule created: Day " + savedSchedule.getDayOfWeek() + 
                " Period " + savedSchedule.getPeriod() + " (ID: " + savedSchedule.getId() + ")");
            
            return ResponseEntity.ok(Map.of(
                "message", "Schedule created successfully",
                "schedule", savedSchedule
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to create schedule: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSchedule(@PathVariable Integer id, @RequestBody Map<String, Object> scheduleData) {
        try {
            Schedule existingSchedule = scheduleService.getScheduleById(id);
            if (existingSchedule == null) {
                return ResponseEntity.notFound().build();
            }
            
            // Update fields
            if (scheduleData.get("dayOfWeek") != null) {
                existingSchedule.setDayOfWeek((Integer) scheduleData.get("dayOfWeek"));
            }
            
            if (scheduleData.get("period") != null) {
                existingSchedule.setPeriod((Integer) scheduleData.get("period"));
            }
            
            if (scheduleData.get("room") != null) {
                existingSchedule.setRoom((String) scheduleData.get("room"));
            }
            
            // Update class
            if (scheduleData.get("classId") != null) {
                Integer classId = (Integer) scheduleData.get("classId");
                Optional<ClassEntity> classEntity = classRepository.findById(classId);
                if (classEntity.isPresent()) {
                    existingSchedule.setClassEntity(classEntity.get());
                } else {
                    return ResponseEntity.badRequest().body(Map.of("error", "Invalid class ID"));
                }
            }
            
            // Update subject
            if (scheduleData.get("subjectId") != null) {
                Integer subjectId = (Integer) scheduleData.get("subjectId");
                Optional<Subject> subject = subjectRepository.findById(subjectId);
                if (subject.isPresent()) {
                    existingSchedule.setSubject(subject.get());
                } else {
                    return ResponseEntity.badRequest().body(Map.of("error", "Invalid subject ID"));
                }
            }
            
            // Update teacher
            if (scheduleData.get("teacherId") != null) {
                Integer teacherId = (Integer) scheduleData.get("teacherId");
                Optional<User> teacher = userRepository.findById(teacherId);
                if (teacher.isPresent()) {
                    existingSchedule.setTeacher(teacher.get());
                } else {
                    return ResponseEntity.badRequest().body(Map.of("error", "Invalid teacher ID"));
                }
            }
            
            Schedule updatedSchedule = scheduleService.saveSchedule(existingSchedule);
            
            // Logging: Log schedule update
            System.out.println("Schedule updated: Day " + updatedSchedule.getDayOfWeek() + 
                " Period " + updatedSchedule.getPeriod() + " (ID: " + updatedSchedule.getId() + ")");
            
            return ResponseEntity.ok(Map.of(
                "message", "Schedule updated successfully",
                "schedule", updatedSchedule
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to update schedule: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSchedule(@PathVariable Integer id) {
        try {
            Schedule schedule = scheduleService.getScheduleById(id);
            if (schedule == null) {
                return ResponseEntity.notFound().build();
            }
            
            scheduleService.deleteSchedule(id);
            
            // Logging: Log schedule deletion
            System.out.println("Schedule deleted: Day " + schedule.getDayOfWeek() + 
                " Period " + schedule.getPeriod() + " (ID: " + schedule.getId() + ")");
            
            return ResponseEntity.ok(Map.of("message", "Schedule deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to delete schedule: " + e.getMessage()));
        }
    }
}



