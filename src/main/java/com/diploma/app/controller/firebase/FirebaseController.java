//package com.diploma.app.controller.firebase;
//
//import com.diploma.app.firebase.FirebaseService;
//import com.diploma.app.firebase.PasswordChangeNotification;
//import com.google.firebase.messaging.FirebaseMessagingException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/firebase/")
//public class FirebaseController {
//
//    private final FirebaseService firebaseService;
//
//    @Autowired
//    public FirebaseController(FirebaseService firebaseService) {
//        this.firebaseService = firebaseService;
//    }
//
//    @RequestMapping("/password-change")
//    @ResponseBody
//    public String sendPasswordChangeNotification(@RequestParam("token") String token) throws FirebaseMessagingException {
//        PasswordChangeNotification passwordChangeNotification = new PasswordChangeNotification();
//        passwordChangeNotification.setSubject("Security");
//        passwordChangeNotification.setContent("Password change detected. Please login again.");
//        return firebaseService.sendPasswordChangeNotification(passwordChangeNotification, token);
//    }
//
//
//}
