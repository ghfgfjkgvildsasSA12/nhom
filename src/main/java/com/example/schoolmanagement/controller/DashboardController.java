package com.example.schoolmanagement.controller;

import com.example.schoolmanagement.entity.User;
import com.example.schoolmanagement.service.AiService;
import com.example.schoolmanagement.service.SchoolService;
import com.example.schoolmanagement.service.UserService;
import com.example.schoolmanagement.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private UserService userService;
    @Autowired
    private AiService aiService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        model.addAttribute("user", user);

        String role = user.getRole().getName();
        switch (role) {
            case "SUPER_ADMIN":
                model.addAttribute("schools", schoolService.getAllSchools());
                return "dashboard/super-admin";
            case "ADMIN":
                String prediction = aiService.predictEnrollment(user.getSchool());
                model.addAttribute("prediction", prediction);
                return "dashboard/admin";
            case "TEACHER":
                model.addAttribute("schedules", scheduleService.getAllSchedules().stream()
                        .filter(s -> s.getTeacher().getId().equals(user.getId()))
                        .toList());
                return "dashboard/teacher";
            case "STUDENT":
                String learningPath = aiService.suggestLearningPath(user);
                model.addAttribute("learningPath", learningPath);
                return "dashboard/student";
            default:
                return "redirect:/login";
        }
    }
}