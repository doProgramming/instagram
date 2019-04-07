package com.example.demo.email;

import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Override
    public Boolean sendEmail(String subject, String body, String receiverName, String senderName, String sendEmailTo) {
        new EmailServer().sendMail(subject, body, receiverName, senderName, sendEmailTo);
        return true;
    }
}
