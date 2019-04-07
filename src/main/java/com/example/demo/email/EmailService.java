package com.example.demo.email;

public interface EmailService {
    Boolean sendEmail(String subject, String body, String receiverName, String senderName, String sendEmailTo);
}
