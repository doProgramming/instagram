package com.example.demo.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "email")
public class EmailEndpoint {

    @Autowired
    EmailService emailService;

    @GetMapping
    public void sendEmail(@RequestParam String subject, @RequestParam String body, @RequestParam(required = false) String receiverName, @RequestParam String senderName, @RequestParam String sendEmailTo) {
        emailService.sendEmail(subject, body, receiverName, senderName, sendEmailTo);
    }
}
