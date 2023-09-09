package com.example.registerapp.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPasswordResponse {
	private boolean success;
	private String message;

	// Constructors, getters, and setters
}
