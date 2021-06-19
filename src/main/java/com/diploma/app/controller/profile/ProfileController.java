package com.diploma.app.controller.profile;

import com.diploma.app.controller.auth.AuthController;
import com.diploma.app.dto.ProfileChangeDto;
import com.diploma.app.dto.PasswordChangeDto;
//import com.diploma.app.firebase.FirebaseService;
import com.diploma.app.model.Users;
import com.diploma.app.security.jwt.JwtTokenProvider;
import com.diploma.app.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public ProfileController(
            PasswordEncoder passwordEncoder,
            UserService userService,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        //this.firebaseService = firebaseService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("profile")
    @ApiOperation(value = "Get user profile", consumes = "Nothing", response = Users.class, produces = "Users object")
    public Users getProfile() {
        return userService.findByUserName(getUserDetails().getUsername());
    }

    @PostMapping("profile/password/edit")
    @ApiOperation(value = "Change password", response = String.class, produces = "Operation result response")
    public ResponseEntity<Map<String, String>> changePassword(@RequestBody PasswordChangeDto passwordChangeDto) {
        Map<String, String> response = new HashMap<>();
        Users user = userService.findByUserName(getUserDetails().getUsername());
        if (passwordEncoder.matches(passwordChangeDto.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(passwordChangeDto.getNewPassword()));
            user.setPasswordLastChangedDate(new Date());
            userService.save(user);
            String newToken = jwtTokenProvider.createToken(user.getUserName(), user.getRoles());
            response.put("response", newToken);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect password");
        }
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("profile/edit")
    @ApiOperation(value = "Email password", response = String.class, produces = "Operation result response")
    public ResponseEntity<Map<String, String>> changeEmail(@RequestBody ProfileChangeDto profileChangeDto) {
        Map<String, String> response = new HashMap<>();
        Users user = userService.findByUserName(getUserDetails().getUsername());
        if (!user.getUserName().equals(profileChangeDto.getUserName()) && userService.existsByUserName(profileChangeDto.getUserName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username");
        }
        if (!user.getEmail().equals(profileChangeDto.getEmail()) && userService.existsByEmail(profileChangeDto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email");
        }
        user.setFirstName(profileChangeDto.getFirstName());
        user.setLastName(profileChangeDto.getLastName());
        user.setEmail(profileChangeDto.getEmail());
        userService.save(user);
        response.put("response", "Success");
        return ResponseEntity.ok().body(response);
    }

    private UserDetails getUserDetails() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }



}
