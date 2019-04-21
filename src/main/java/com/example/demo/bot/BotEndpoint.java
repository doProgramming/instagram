package com.example.demo.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BotEndpoint {

    @Autowired
    BotService botService;

    @GetMapping(value = "/sendMails")
    public void sendEmails(@RequestParam String message) {
        botService.sendGroupOfEmails(message);
    }
}
