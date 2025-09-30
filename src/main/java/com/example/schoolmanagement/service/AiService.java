package com.example.schoolmanagement.service;

import com.example.schoolmanagement.entity.Record;
import com.example.schoolmanagement.entity.User;
import com.example.schoolmanagement.entity.School;
import com.example.schoolmanagement.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AiService {
    @Autowired
    private RecordRepository recordRepository;

    public String suggestLearningPath(User student) {
        double avgScore = recordRepository.findAll().stream()
                .filter(r -> r.getStudent().getId().equals(student.getId()) && "EXAM".equals(r.getType()))
                .mapToDouble(Record::getValue)
                .average().orElse(0.0);
        String path = avgScore > 8 ? "Nâng cao kiến thức" : "Củng cố cơ bản";
        logAiAction(student, path);
        return path;
    }

    public String suggestComment(double score) {
        return score >= 8 ? "Xuất sắc!" : score >= 5 ? "Tạm ổn." : "Cần cố gắng.";
    }

    public String predictEnrollment(School school) {
        long current = recordRepository.findAll().stream()
                .filter(r -> r.getSchool() != null && r.getSchool().getId().equals(school.getId()))
                .count();
        return "Dự báo sĩ số năm sau: " + (current + 10);
    }

    private void logAiAction(User student, String action) {
        Record log = new Record();
        log.setType("AI");
        log.setStudent(student);
        log.setNote(action);
        recordRepository.save(log);
    }
}