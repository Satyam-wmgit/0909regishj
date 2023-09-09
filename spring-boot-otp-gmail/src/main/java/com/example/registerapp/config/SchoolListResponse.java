package com.example.registerapp.config;

import java.util.List;

import com.example.registerapp.entity.School;

import lombok.Data;
@Data
public class SchoolListResponse {
    private List<School> schools;

    public SchoolListResponse(List<School> schools) {
        this.schools = schools;
    }

  
}
