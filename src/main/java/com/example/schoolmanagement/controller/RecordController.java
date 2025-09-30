/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.schoolmanagement.controller;

import com.example.schoolmanagement.entity.Record;
import com.example.schoolmanagement.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/records")
public class RecordController {
    @Autowired
    private RecordService recordService;

    @GetMapping
    public String listRecords(Model model) {
        model.addAttribute("records", recordService.getAllRecords());
        return "management/records";
    }

    @GetMapping("/new")
    public String newRecord(Model model) {
        model.addAttribute("record", new Record());
        return "management/record-form";
    }

    @PostMapping
    public String saveRecord(@ModelAttribute Record record) {
        recordService.saveRecord(record);
        return "redirect:/records";
    }

    @GetMapping("/edit/{id}")
    public String editRecord(@PathVariable Integer id, Model model) {
        model.addAttribute("record", recordService.getRecordById(id));
        return "management/record-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteRecord(@PathVariable Integer id) {
        recordService.deleteRecord(id);
        return "redirect:/records";
    }
}
