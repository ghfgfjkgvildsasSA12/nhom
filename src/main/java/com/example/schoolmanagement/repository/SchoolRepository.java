package com.example.schoolmanagement.repository;

import com.example.schoolmanagement.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SchoolRepository extends JpaRepository<School, Integer> {
    Optional<School> findByCode(String code);
}