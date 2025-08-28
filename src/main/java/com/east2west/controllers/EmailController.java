package com.east2west.controllers;

import java.util.HashMap;
import java.util.Map;

import com.east2west.models.DTO.UserEmail;
import com.east2west.service.EmailService;
import jakarta.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> sendEmail(@RequestBody UserEmail user) throws MessagingException{
        Map<String, Object> map = new HashMap<>();
        map.put("message", emailService.sendEmail(user));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
