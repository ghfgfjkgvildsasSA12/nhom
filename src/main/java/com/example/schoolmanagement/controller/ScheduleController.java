/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.schoolmanagement.controller;

import com.example.schoolmanagement.entity.Schedule;
import com.example.schoolmanagement.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/schedules")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    @GetMapping
    public String listSchedules(Model model) {
        model.addAttribute("schedules", scheduleService.getAllSchedules());
        return "management/schedules";
    }

    @GetMapping("/new")
    public String newSchedule(Model model) {
        model.addAttribute("schedule", new Schedule());
        return "management/schedule-form";
    }

    @PostMapping
    public String saveSchedule(@ModelAttribute Schedule schedule) {
        scheduleService.saveSchedule(schedule);
        return "redirect:/schedules";
    }

    @GetMapping("/edit/{id}")
    public String editSchedule(@PathVariable Integer id, Model model) {
        model.addAttribute("schedule", scheduleService.getScheduleById(id));
        return "management/schedule-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteSchedule(@PathVariable Integer id) {
        scheduleService.deleteSchedule(id);
        return "redirect:/schedules";
    }
}
