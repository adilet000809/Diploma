package com.diploma.app.controller.profile;

import com.diploma.app.controller.auth.AuthController;
import com.diploma.app.dto.EmailChangeDto;
import com.diploma.app.dto.PasswordChangeDto;
//import com.diploma.app.firebase.FirebaseService;
import com.diploma.app.firebase.PasswordChangeNotification;
import com.diploma.app.model.Users;
import com.diploma.app.service.UserService;
import com.google.firebase.messaging.FirebaseMessagingException;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/user/")
public class ProfileController {

    private final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    //private final FirebaseService firebaseService;

    @Autowired
    public ProfileController(PasswordEncoder passwordEncoder, UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        //this.firebaseService = firebaseService;
    }

    @GetMapping("profile")
    @ApiOperation(value = "Get user profile", consumes = "Nothing", response = Users.class, produces = "Users object")
    public Users getProfile() {
        return userService.findByUserName(getUserDetails().getUsername());
    }

    @PostMapping("profile/password/change")
    @ApiOperation(value = "Change password", response = String.class, produces = "Operation result response")
    public ResponseEntity<Map<String, String>> changePassword(@RequestBody PasswordChangeDto passwordChangeDto) {
        Map<String, String> response = new HashMap<>();
        Users user = userService.findByUserName(getUserDetails().getUsername());
        if (passwordEncoder.matches(passwordChangeDto.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(passwordChangeDto.getNewPassword()));
            user.setPasswordLastChangedDate(new Date());
            userService.update(user);
            response.put("response", "Password changed successfully");
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect password");
        }
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("profile/email/change")
    @ApiOperation(value = "Email password", response = String.class, produces = "Operation result response")
    public ResponseEntity<Map<String, String>> changeEmail(@RequestBody EmailChangeDto emailChangeDto) {
        Map<String, String> response = new HashMap<>();
        Users user = userService.findByUserName(getUserDetails().getUsername());
        if (passwordEncoder.matches(emailChangeDto.getPassword(), user.getPassword())) {
            user.setEmail(emailChangeDto.getEmail());
            user.setUpdated(new Date());
            userService.update(user);
            response.put("response", "Email has been changed successfully");
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect password");
        }
        return ResponseEntity.ok().body(response);
    }

    private UserDetails getUserDetails() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }



}
