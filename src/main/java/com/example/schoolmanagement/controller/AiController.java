package com.example.schoolmanagement.controller;

import com.example.schoolmanagement.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AiController {
    @Autowired
    private AiService aiService;

    @GetMapping("/ai/suggest-comment")
    public String suggestComment(@RequestParam double score) {
        return aiService.suggestComment(score);
    }
}