package com.example.demo.email;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);
    //Boolean sendEmail(String subject, String body, String receiverName, String senderName, String sendEmailTo);
}
