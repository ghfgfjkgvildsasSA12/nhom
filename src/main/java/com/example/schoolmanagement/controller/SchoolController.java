package com.example.schoolmanagement.controller;

import com.example.schoolmanagement.entity.School;
import com.example.schoolmanagement.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SchoolController {
    @Autowired
    private SchoolService schoolService;

    @GetMapping("/schools")
    public String listSchools(Model model) {
        model.addAttribute("schools", schoolService.getAllSchools());
        return "management/schools";
    }

    @GetMapping("/schools/new")
    public String newSchool(Model model) {
        model.addAttribute("school", new School());
        return "management/school-form";
    }

    @PostMapping("/schools")
    public String saveSchool(@ModelAttribute("school") School school) {
        schoolService.saveSchool(school);
        return "redirect:/schools";
    }

    @GetMapping("/schools/edit/{id}")
    public String editSchool(@PathVariable Integer id, Model model) {
        model.addAttribute("school", schoolService.getSchoolById(id));
        return "management/school-form";
    }

    @GetMapping("/schools/delete/{id}")
    public String deleteSchool(@PathVariable Integer id) {
        schoolService.deleteSchool(id);
        return "redirect:/schools";
    }
}