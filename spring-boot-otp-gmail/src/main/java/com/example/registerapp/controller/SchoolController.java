package com.example.registerapp.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.registerapp.config.SchoolListResponse;
import com.example.registerapp.entity.School;
import com.example.registerapp.repository.SchoolRepository;

@RestController
@RequestMapping("/schools")
public class SchoolController {
    private final SchoolRepository schoolRepository;

    @Autowired
    public SchoolController(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;
    }

    @PostMapping("/save")
    public School createSchool(@RequestBody School school) {
      
        return schoolRepository.save(school);
    }
    
    @GetMapping("/get")
    public SchoolListResponse getAllSchools() {
        List<School> schools = schoolRepository.findAll();
        return new SchoolListResponse(schools);
    }
}
