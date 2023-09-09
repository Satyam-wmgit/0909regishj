package com.example.registerapp.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.registerapp.config.ForgotPasswordResponse;
import com.example.registerapp.config.LoginResponse;
import com.example.registerapp.config.OtpResponse;
import com.example.registerapp.config.RegistrationResponse;
import com.example.registerapp.config.VerificationResponse;
import com.example.registerapp.dto.LoginDto;
import com.example.registerapp.dto.RegisterDto;
import com.example.registerapp.entity.User;
import com.example.registerapp.repository.UserRepository;
import com.example.registerapp.util.EmailUtil;
import com.example.registerapp.util.OtpUtil;

import jakarta.mail.MessagingException;

@Service
public class UserService {

	@Autowired
	private OtpUtil otpUtil;
	@Autowired
	private EmailUtil emailUtil;
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public RegistrationResponse register(RegisterDto registerDto) {
		Optional<User> users = userRepository.findByEmail(registerDto.getEmail());
		if (users.isPresent()) {
			String mail = users.get().getEmail();
			if (mail.equals(registerDto.getEmail())) {
				return new RegistrationResponse(false, "User already registered", null);
			}
		}

		/*
		 * // it's for the real otp via email String otp = otpUtil.generateOtp(); try {
		 * emailUtil.sendOtpEmail(registerDto.getEmail(), otp); } catch
		 * (MessagingException e) { throw new
		 * RuntimeException("Unable to send otp please try again"); }
		 */

		String otp = "4321"; // For testing

		User user = new User();

		user.setFirstName(registerDto.getFirstName());
		user.setLastName(registerDto.getLastName());
		user.setPhone(registerDto.getPhone());
		user.setDefaultSchoolId(registerDto.getDefaultSchoolId());
		user.setHeight(registerDto.getHeight());
		user.setWeight(registerDto.getWeight());
		user.setEmail(registerDto.getEmail());

		user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
		user.setConfirmPassword(null);

		user.setOtp(otp);
		user.setOtpGeneratedTime(LocalDateTime.now());

		if (registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
			User savedUser = userRepository.save(user);
			return new RegistrationResponse(true, "User registered successfully", savedUser);
		} else {
			return new RegistrationResponse(false, "Password does not match", null);
		}
	}

	public VerificationResponse verifyAccount(String email, String otp) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
		VerificationResponse response = new VerificationResponse();

		if (user.getOtp().equals(otp)
				&& Duration.between(user.getOtpGeneratedTime(), LocalDateTime.now()).getSeconds() < (1 * 60)) {
			user.setTextable(true);
			userRepository.save(user);
			response.setSuccess(true);
			response.setMessage("OTP verified. You can now log in.");
			response.setUser(user); // Optionally, include user details in the response
		} else if (!user.getOtp().equals(otp)) {
			response.setSuccess(false);
			response.setMessage("OTP is incorrect");
		} else {
			response.setSuccess(false);
			response.setMessage("Please regenerate OTP and try again");
		}

		return response;
	}

	public OtpResponse regenerateOtp(String email) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
		
		/*  // it's for the regenerate otp via email
		 * String otp = otpUtil.generateOtp(); try { emailUtil.sendOtpEmail(email, otp);
		 * } catch (MessagingException e) { throw new
		 * RuntimeException("Unable to send OTP, please try again"); }
		 */
		
		String otp ="4321";
		user.setOtp(otp);
		user.setOtpGeneratedTime(LocalDateTime.now());
		userRepository.save(user);

		OtpResponse response = new OtpResponse();
		response.setSuccess(true);
		response.setMessage("Email sent... please verify your account within 1 minute");

		return response;
	}

	public LoginResponse login(LoginDto loginDto) {
		Optional<User> user = userRepository.findByEmail(loginDto.getEmail());
		if (user.isEmpty()) {
			return new LoginResponse(false, "User not found with this email");
		}

		if (passwordEncoder.matches(loginDto.getPassword(), user.get().getPassword()) && user.get().isTextable()) {
			return new LoginResponse(true, "User login successful");
		}

		return new LoginResponse(false, "Password is incorrect or the account is not verified");
	}
	
	public ForgotPasswordResponse sendForgotPasswordEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

			/*
			 * // Generate a new OTP String otp = otpUtil.generateOtp();
			 * 
			 * try { // Send the OTP via email (use your email sending logic here)
			 * emailUtil.sendOtpEmail(email, otp); } catch (MessagingException e) { return
			 * new ForgotPasswordResponse(false, "Unable to send OTP. Please try again."); }
			 */
            String otp="1234";

            // Update the user's OTP and OTP generation time
            user.setOtp(otp);
            user.setOtpGeneratedTime(LocalDateTime.now());
            userRepository.save(user);

            return new ForgotPasswordResponse(true, "OTP sent successfully. Please check your email for the OTP.");
        } else {
            return new ForgotPasswordResponse(false, "User not found with this email address.");
        }
    }
}
