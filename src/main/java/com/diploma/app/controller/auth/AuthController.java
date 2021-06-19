package com.diploma.app.controller.auth;

import com.diploma.app.dto.AuthRequestDto;
import com.diploma.app.dto.ForgotPasswordDto;
import com.diploma.app.dto.PasswordResetDto;
import com.diploma.app.dto.RegistrationDto;
import com.diploma.app.exceptions.NotUniqueUserException;
import com.diploma.app.model.*;
import com.diploma.app.security.jwt.JwtTokenProvider;
import com.diploma.app.service.EmailService;
import com.diploma.app.service.PasswordResetTokenService;
import com.diploma.app.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.util.*;

@RestController
@RequestMapping(value = "/api/auth/")
public class AuthController {

    private final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
    private final  String PASSWORD_VALIDITY_REGEX = "^(?=.*[0-9]).{8,}$";

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final PasswordResetTokenService passwordResetTokenService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final SecureRandom secureRandom;

    @Autowired
    public AuthController(
            AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider,
            UserService userService,
            PasswordResetTokenService passwordResetTokenService,
            EmailService emailService,
            PasswordEncoder passwordEncoder, SecureRandom secureRandom) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.passwordResetTokenService = passwordResetTokenService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.secureRandom = secureRandom;
    }

    @PostMapping("login")
    @ApiOperation(value = "Login", response = Map.class, produces = "Username and JWT in json format")
    public ResponseEntity<Map<String, Object>> login(@RequestBody AuthRequestDto authRequestDto) {
        try {
            String userName = authRequestDto.getUserName();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, authRequestDto.getPassword()));
            Users user = userService.findByUserName(userName);

            if (user == null) {
                throw new UsernameNotFoundException("User with username: " + userName + " not found");
            }

            String token = jwtTokenProvider.createToken(userName, user.getRoles());

            Map<String, Object> response = new HashMap<>();
            response.put("user", user);
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect username or password");
        }
    }

    @PostMapping("register")
    @ApiOperation(value = "register", response = String.class, produces = "Response about result of the operation")
    public ResponseEntity<Map<String, String>> register(@RequestBody RegistrationDto registrationDTO) {
        Map<String, String> response = new HashMap<>();
        if (!userService.existsByEmail(registrationDTO.getEmail())) {
            if (!userService.existsByUserName(registrationDTO.getUserName())) {
                if (isValidPassword(registrationDTO.getPassword())) {
                    Users user = new Users();
                    user.setFirstName(registrationDTO.getFirstName());
                    user.setLastName(registrationDTO.getLastName());
                    user.setUserName(registrationDTO.getUserName());
                    user.setEmail(registrationDTO.getEmail());
                    user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
                    userService.register(user);
                    response.put("response", "User has been registered successfully");
                    return ResponseEntity.ok(response);
                }
                else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password validation failed");
                }
            }
            else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username exists", new NotUniqueUserException("Username exists"));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email exists", new NotUniqueUserException("Email exists"));
        }
    }

    @PostMapping("forgot")
    @ApiOperation(value = "Forgot password", response = String.class, consumes = "User email", produces = "Send email with token to reset password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody ForgotPasswordDto forgotPasswordDto) {
        Map<String, String> response = new HashMap<>();
        Users user = userService.findByEmail(forgotPasswordDto.getEmail());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with email:" + forgotPasswordDto.getEmail() + "not found");
        }

        passwordResetTokenService.deleteAllByUser(user);
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken((secureRandom.nextInt(900000) + 100000) + "");
        passwordResetToken.setExpiration();
        passwordResetToken.setUser(user);
        passwordResetTokenService.save(passwordResetToken);
        emailService.sendEmail(constructEmail(user.getEmail(), passwordResetToken));
        response.put("response", "Code for reset is sent to your email");
        return ResponseEntity.ok().body(response);

    }

    @PostMapping("reset")
    @ApiOperation(value = "Reset password", response = String.class, consumes = "PasswordResetDTO object", produces = "Response about result of the operation")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody PasswordResetDto passwordResetDto) {
        Map<String, String> response = new HashMap<>();
        Users user = userService.findByEmail(passwordResetDto.getEmail());
        if (user == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with email:" + passwordResetDto.getEmail() + "not found");
        }
        PasswordResetToken passwordResetToken = passwordResetTokenService.findByUser(user);
        if (passwordResetToken == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid attempt to reset password");
        }

        if (passwordResetToken.isExpired()) {
            passwordResetTokenService.deleteAllByUser(passwordResetToken.getUser());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password reset time is up");
        } else {
            if (passwordResetToken.getAttempts()>0) {
                if (passwordResetDto.getCode().equals(passwordResetToken.getToken())) {
                    user.setPassword(passwordEncoder.encode(passwordResetDto.getNewPassword()));
                    user.setPasswordLastChangedDate(new Date());
                    passwordResetTokenService.deleteAllByUser(passwordResetToken.getUser());
                    userService.update(user);
                    response.put("response", "Password changed successfully.");
                    LOGGER.trace("User: " + user.getUserName() + " reset password in attempts:" + (4-passwordResetToken.getAttempts()));
                    return ResponseEntity.ok().body(response);
                } else {
                    passwordResetToken.setAttempts(passwordResetToken.getAttempts()-1);
                    passwordResetTokenService.save(passwordResetToken);
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect code. " + passwordResetToken.getAttempts() + " attempt(s) left.");
                }
            } else {
                passwordResetTokenService.deleteAllByUser(passwordResetToken.getUser());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "All attempts run out. Try again.");
            }

        }

    }

    private SimpleMailMessage constructEmail(String userEmail, PasswordResetToken token) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject("Password reset");
        email.setText(token.getToken());
        email.setTo(userEmail);
        return email;
    }

    private boolean isValidPassword(String password) {
        return password.matches(PASSWORD_VALIDITY_REGEX);
    }

}
