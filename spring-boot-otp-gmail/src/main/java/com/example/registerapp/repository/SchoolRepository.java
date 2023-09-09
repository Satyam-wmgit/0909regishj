package com.example.registerapp.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.registerapp.entity.School;

public interface SchoolRepository extends JpaRepository<School, Integer> {
}
