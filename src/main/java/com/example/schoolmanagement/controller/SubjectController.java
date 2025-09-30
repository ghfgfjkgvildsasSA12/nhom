/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.schoolmanagement.controller;

import com.example.schoolmanagement.entity.Subject;
import com.example.schoolmanagement.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/subjects")
public class SubjectController {
    @Autowired
    private SubjectService subjectService;

    @GetMapping
    public String listSubjects(Model model) {
        model.addAttribute("subjects", subjectService.getAllSubjects());
        return "management/subjects";
    }

    @GetMapping("/new")
    public String newSubject(Model model) {
        model.addAttribute("subject", new Subject());
        return "management/subject-form";
    }

    @PostMapping
    public String saveSubject(@ModelAttribute Subject subject) {
        subjectService.saveSubject(subject);
        return "redirect:/subjects";
    }

    @GetMapping("/edit/{id}")
    public String editSubject(@PathVariable Integer id, Model model) {
        model.addAttribute("subject", subjectService.getSubjectById(id));
        return "management/subject-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteSubject(@PathVariable Integer id) {
        subjectService.deleteSubject(id);
        return "redirect:/subjects";
    }
}
