/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.schoolmanagement.controller;

import com.example.schoolmanagement.entity.Announcement;
import com.example.schoolmanagement.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/announcements")
public class AnnouncementController {
    @Autowired
    private AnnouncementService announcementService;

    @GetMapping
    public String listAnnouncements(Model model) {
        model.addAttribute("announcements", announcementService.getAllAnnouncements());
        return "management/announcements";
    }

    @GetMapping("/new")
    public String newAnnouncement(Model model) {
        model.addAttribute("announcement", new Announcement());
        return "management/announcement-form";
    }

    @PostMapping
    public String saveAnnouncement(@ModelAttribute Announcement announcement) {
        announcementService.saveAnnouncement(announcement);
        return "redirect:/announcements";
    }

    @GetMapping("/edit/{id}")
    public String editAnnouncement(@PathVariable Integer id, Model model) {
        model.addAttribute("announcement", announcementService.getAnnouncementById(id));
        return "management/announcement-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteAnnouncement(@PathVariable Integer id) {
        announcementService.deleteAnnouncement(id);
        return "redirect:/announcements";
    }
}
