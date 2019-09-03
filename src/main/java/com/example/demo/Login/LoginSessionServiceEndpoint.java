package com.example.demo.Login;

import org.brunocvcunha.instagram4j.Instagram4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class LoginSessionServiceEndpoint {

    @Autowired
    LoginSessionService loginSessionService;

    @GetMapping(value = "/login/session")
    public Instagram4j login(@RequestParam String username, @RequestParam String password) throws IOException, ClassNotFoundException {
        return loginSessionService.login(username, password);
    }
}
