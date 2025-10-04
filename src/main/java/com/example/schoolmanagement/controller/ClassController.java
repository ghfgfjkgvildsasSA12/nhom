package com.example.schoolmanagement.controller;

import com.example.schoolmanagement.entity.ClassEntity;
import com.example.schoolmanagement.service.ClassService;
import com.example.schoolmanagement.service.SchoolService;
import com.example.schoolmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ClassController {
    @Autowired
    private ClassService classService;
    
    @Autowired
    private SchoolService schoolService;
    
    @Autowired
    private UserService userService;

    @GetMapping("/classes")
    public String listClasses(Model model) {
        model.addAttribute("classes", classService.getAllClasses());
        return "management/classes";
    }

    @GetMapping("/classes/new")
    public String newClass(Model model) {
        model.addAttribute("classEntity", new ClassEntity());
        model.addAttribute("schools", schoolService.getAllSchools());
        model.addAttribute("teachers", userService.getAllUsers());
        return "management/class-form";
    }

    @PostMapping("/classes")
    public String saveClass(@ModelAttribute("classEntity") ClassEntity classEntity) {
        classService.saveClass(classEntity);
        return "redirect:/classes";
    }

    @GetMapping("/classes/edit/{id}")
    public String editClass(@PathVariable Integer id, Model model) {
        model.addAttribute("classEntity", classService.getClassById(id));
        model.addAttribute("schools", schoolService.getAllSchools());
        model.addAttribute("teachers", userService.getAllUsers());
        return "management/class-form";
    }

    @GetMapping("/classes/delete/{id}")
    public String deleteClass(@PathVariable Integer id) {
        classService.deleteClass(id);
        return "redirect:/classes";
    }
}