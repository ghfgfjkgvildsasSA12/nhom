package com.example.schoolmanagement.repository;

import com.example.schoolmanagement.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Integer> {
}