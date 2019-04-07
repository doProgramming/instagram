package com.example.demo.email;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailServer {

    public void sendMail(String subject, String body, String receiverName, String senderName, String sendEmailTo) {
        final String username = "javadeveloper889@gmail.com";
        final String password = "123456sp*";

        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.mailtrap.io");
        prop.put("mail.smtp.port", "25");
        prop.put("mail.smtp.ssl.trust", "smtp.mailtrap.io");

        if (receiverName == null) {
            receiverName = "Strahinja";
        }

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("javadeveloper889@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(sendEmailTo));
            message.setSubject(subject);
            message.setText("Dear " + receiverName + ","
                    + "\n\n" + body + "\n\n \n\n Best regards, \n\n " + senderName);

            Transport.send(message);

            System.out.println("emailService to" + senderName);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    public void sendMail(String body) {
        String senderName = "instagram";
        String subject = "poslala mejl";
        String receiverName = "nesto";
        final String username = "petrovicstrahinjabgsrb@gmail.com";
        final String password = "123456sp*";

        Properties props = new Properties();
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        if (receiverName == null) {
            receiverName = "Strahinja";
        }

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("petrovicstrahinjabgsrb@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("petrovicstrahinja@yahoo.com"));
            message.setSubject(subject);
            message.setText("Dear " + receiverName + ","
                    + "\n\n" + body + "\n\n \n\n Best regards, \n\n " + senderName);

            Transport.send(message);

            System.out.println("emailService to" + senderName);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }
}
