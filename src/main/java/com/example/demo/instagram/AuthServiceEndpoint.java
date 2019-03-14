package com.example.demo.instagram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class AuthServiceEndpoint {

    @Autowired
    Auth auth;

    @GetMapping(value = "/instagram")
    public UserData getDataFromInstagram(@RequestParam String username, @RequestParam String password, @RequestParam String getDataFromUser) throws IOException {
        return auth.login(username, password, getDataFromUser);
    }

}
