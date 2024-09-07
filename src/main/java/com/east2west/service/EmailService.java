package com.east2west.service;

import com.east2west.models.DTO.UserEmail;
import jakarta.mail.MessagingException;


public interface EmailService {

    String sendEmail(UserEmail user) throws MessagingException;
}
