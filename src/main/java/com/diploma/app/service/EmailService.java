package com.diploma.app.service;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;

public interface EmailService {

    void sendEmail(SimpleMailMessage message);

}
