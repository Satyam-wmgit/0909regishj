package com.example.registerapp.config;

import com.example.registerapp.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationResponse {
    private boolean success;
    private String message;
    private User user;
}
