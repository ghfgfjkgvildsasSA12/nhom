package com.example.schoolmanagement.controller;

import com.example.schoolmanagement.entity.User;
import com.example.schoolmanagement.entity.Role;
import com.example.schoolmanagement.entity.School;
import com.example.schoolmanagement.repository.UserRepository;
import com.example.schoolmanagement.repository.RoleRepository;
import com.example.schoolmanagement.repository.SchoolRepository;
import com.example.schoolmanagement.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        try {
            String email = loginRequest.get("email");
            String password = loginRequest.get("password");

            // Get user details
            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
            }

            User user = userOpt.get();
            
            // Check password (plain text comparison)
            if (!password.equals(user.getPasswordHash())) {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid credentials"));
            }
            
            // Generate JWT token
            String token = jwtUtil.generateToken(email);

            // Prepare response
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", Map.of(
                "id", user.getId(),
                "email", user.getEmail(),
                "fullName", user.getFullName(),
                "status", user.getStatus(),
                "role", Map.of(
                    "id", user.getRole().getId(),
                    "name", user.getRole().getName(),
                    "description", user.getRole().getDescription()
                ),
                "school", user.getSchool() != null ? Map.of(
                    "id", user.getSchool().getId(),
                    "name", user.getSchool().getName(),
                    "code", user.getSchool().getCode()
                ) : null
            ));

            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid credentials"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", "Login failed: " + e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> registerRequest) {
        try {
            String email = registerRequest.get("email");
            String password = registerRequest.get("password");
            String fullName = registerRequest.get("fullName");

            // Check if user already exists
            if (userRepository.findByEmail(email).isPresent()) {
                return ResponseEntity.badRequest().body(Map.of("error", "User already exists"));
            }

            // Create new user (default role will be STUDENT)
            User user = new User();
            user.setEmail(email);
            user.setPasswordHash(passwordEncoder.encode(password));
            user.setFullName(fullName);
            user.setStatus("ACTIVE");
            
            // Set default role as STUDENT
            Optional<Role> studentRole = roleRepository.findByName("STUDENT");
            if (studentRole.isPresent()) {
                user.setRole(studentRole.get());
            }
            
            // Set default school (first school in database)
            Optional<School> defaultSchool = schoolRepository.findAll().stream().findFirst();
            if (defaultSchool.isPresent()) {
                user.setSchool(defaultSchool.get());
            }
            
            userRepository.save(user);

            return ResponseEntity.ok(Map.of("message", "User registered successfully"));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Registration failed"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // For JWT, logout is handled on client side by removing token
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            String email = jwtUtil.extractUsername(token);
            if (jwtUtil.validateToken(token, email)) {
                Optional<User> userOpt = userRepository.findByEmail(email);
                
                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    return ResponseEntity.ok(Map.of(
                        "valid", true,
                        "user", Map.of(
                            "id", user.getId(),
                            "email", user.getEmail(),
                            "fullName", user.getFullName(),
                            "role", user.getRole().getName()
                        )
                    ));
                }
            }
            
            return ResponseEntity.badRequest().body(Map.of("valid", false));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("valid", false));
        }
    }
}
