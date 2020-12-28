package com.diploma.app.firebase;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
public class PasswordChangeNotification {
    private String subject;
    private String content;
}
