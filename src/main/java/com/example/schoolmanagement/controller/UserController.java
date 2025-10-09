package com.example.schoolmanagement.controller;

import com.example.schoolmanagement.entity.User;
import com.example.schoolmanagement.entity.Role;
import com.example.schoolmanagement.entity.School;
import com.example.schoolmanagement.service.UserService;
import com.example.schoolmanagement.repository.RoleRepository;
import com.example.schoolmanagement.repository.SchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @GetMapping
    public ResponseEntity<?> getUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(Map.of("users", users));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to fetch users"));
        }
    }

    @GetMapping("/school/{schoolId}")
    public ResponseEntity<?> getUsersBySchool(@PathVariable Integer schoolId) {
        try {
            // This would need to be implemented in UserService
            List<User> users = userService.getAllUsers(); // For now, return all users
            return ResponseEntity.ok(Map.of("users", users));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to fetch users by school"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Integer id) {
        try {
            User user = userService.getUserById(id);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to fetch user"));
        }
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody Map<String, Object> userData) {
        try {
            User user = new User();
            
            // Set basic fields
            user.setEmail((String) userData.get("email"));
            user.setFullName((String) userData.get("fullName"));
            user.setStatus((String) userData.getOrDefault("status", "ACTIVE"));
            
            // Set password
            String password = (String) userData.get("password");
            if (password != null) {
                user.setPasswordHash(password); // Will be encoded in service
            }
            
            // Set role
            Integer roleId = (Integer) userData.get("roleId");
            if (roleId != null) {
                Optional<Role> role = roleRepository.findById(roleId);
                if (role.isPresent()) {
                    user.setRole(role.get());
                } else {
                    return ResponseEntity.badRequest().body(Map.of("error", "Invalid role ID"));
                }
            }
            
            // Set school
            Integer schoolId = (Integer) userData.get("schoolId");
            if (schoolId != null) {
                Optional<School> school = schoolRepository.findById(schoolId);
                if (school.isPresent()) {
                    user.setSchool(school.get());
                } else {
                    return ResponseEntity.badRequest().body(Map.of("error", "Invalid school ID"));
                }
            }
            
            // Validation: Check if email already exists
            if (userService.findByEmail(user.getEmail()) != null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Email already exists"));
            }
            
            User savedUser = userService.saveUser(user);
            
            // Logging: Log user creation
            System.out.println("User created: " + savedUser.getFullName() + " (ID: " + savedUser.getId() + ")");
            
            return ResponseEntity.ok(Map.of(
                "message", "User created successfully",
                "user", savedUser
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to create user: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody Map<String, Object> userData) {
        try {
            User existingUser = userService.getUserById(id);
            if (existingUser == null) {
                return ResponseEntity.notFound().build();
            }
            
            // Update fields
            if (userData.get("email") != null) {
                String newEmail = (String) userData.get("email");
                // Check if email is unique (excluding current user)
                User userWithEmail = userService.findByEmail(newEmail);
                if (userWithEmail != null && !userWithEmail.getId().equals(id)) {
                    return ResponseEntity.badRequest().body(Map.of("error", "Email already exists"));
                }
                existingUser.setEmail(newEmail);
            }
            
            if (userData.get("fullName") != null) {
                existingUser.setFullName((String) userData.get("fullName"));
            }
            
            if (userData.get("status") != null) {
                existingUser.setStatus((String) userData.get("status"));
            }
            
            if (userData.get("password") != null) {
                existingUser.setPasswordHash((String) userData.get("password"));
            }
            
            // Update role
            if (userData.get("roleId") != null) {
                Integer roleId = (Integer) userData.get("roleId");
                Optional<Role> role = roleRepository.findById(roleId);
                if (role.isPresent()) {
                    existingUser.setRole(role.get());
                } else {
                    return ResponseEntity.badRequest().body(Map.of("error", "Invalid role ID"));
                }
            }
            
            // Update school
            if (userData.get("schoolId") != null) {
                Integer schoolId = (Integer) userData.get("schoolId");
                Optional<School> school = schoolRepository.findById(schoolId);
                if (school.isPresent()) {
                    existingUser.setSchool(school.get());
                } else {
                    return ResponseEntity.badRequest().body(Map.of("error", "Invalid school ID"));
                }
            }
            
            User updatedUser = userService.saveUser(existingUser);
            
            // Logging: Log user update
            System.out.println("User updated: " + updatedUser.getFullName() + " (ID: " + updatedUser.getId() + ")");
            
            return ResponseEntity.ok(Map.of(
                "message", "User updated successfully",
                "user", updatedUser
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to update user: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        try {
            User user = userService.getUserById(id);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }
            
            userService.deleteUser(id);
            
            // Logging: Log user deletion
            System.out.println("User deleted: " + user.getFullName() + " (ID: " + user.getId() + ")");
            
            return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to delete user: " + e.getMessage()));
        }
    }
}

