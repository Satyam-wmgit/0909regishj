package com.example.registerapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.registerapp.config.ForgotPasswordResponse;
import com.example.registerapp.config.LoginResponse;
import com.example.registerapp.config.OtpResponse;
import com.example.registerapp.config.RegistrationResponse;
import com.example.registerapp.config.VerificationResponse;
import com.example.registerapp.dto.ForgotPasswordDto;
import com.example.registerapp.dto.LoginDto;
import com.example.registerapp.dto.RegisterDto;
import com.example.registerapp.service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/register")
	public ResponseEntity<RegistrationResponse> register(@RequestBody RegisterDto registerDto) {
		RegistrationResponse response = userService.register(registerDto);

		if (response.isSuccess()) {
			return ResponseEntity.ok(response);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}

	@PutMapping("/verify-account")
	public ResponseEntity<VerificationResponse> verifyAccount(@RequestParam String email, @RequestParam String otp) {
		VerificationResponse response = userService.verifyAccount(email, otp);

		if (response.isSuccess()) {
			return ResponseEntity.ok(response);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}

	@PutMapping("/regenerate-otp")
	public ResponseEntity<OtpResponse> regenerateOtp(@RequestParam String email) {
		OtpResponse response = userService.regenerateOtp(email);

		if (response.isSuccess()) {
			return ResponseEntity.ok(response);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginDto loginDto) {
		LoginResponse response = userService.login(loginDto);

		if (response.isLoginStatus()) {
			return ResponseEntity.ok(response); // Successful login
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response); // Failed login
		}
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<ForgotPasswordResponse> forgotPassword(@RequestBody ForgotPasswordDto forgotPasswordDto) {
		ForgotPasswordResponse response = userService.sendForgotPasswordEmail(forgotPasswordDto.getEmail());

		if (response.isSuccess()) {
			return ResponseEntity.ok(response);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}
}
