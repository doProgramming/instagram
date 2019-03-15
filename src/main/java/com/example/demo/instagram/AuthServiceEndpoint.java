package com.example.demo.instagram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class AuthServiceEndpoint {

    @Autowired
    Auth auth;

    @GetMapping(value = "/instagram/user/data")
    public UserData getDataFromInstagram(@RequestParam String username, @RequestParam String password
            , @RequestParam String comment, @RequestParam String getDataFromUser, @RequestParam String mediaId) throws IOException {
        return auth.getDataAndSendComment(username, comment, password, getDataFromUser, mediaId);
    }

    @GetMapping(value = "/instagram/user/data/withoutproxy")
    public UserData getDataFromInstagram(@RequestParam String username, @RequestParam String password, @RequestParam String getDataFromUser) throws IOException {
        return auth.oldlogin(username, password, getDataFromUser);
    }

    @PostMapping(value = "/instagram/sendComment")
    public String sendCommentSecured(@RequestBody Authentication authentication) throws IOException {
        return auth.sendComment(authentication);
    }

    @GetMapping(value = "/instagram/sendComment")
    public UserData sendComment(@RequestParam String username, @RequestParam String password
            , @RequestParam String comment, @RequestParam String getDataFromUser,@RequestParam String mediaId) throws IOException {
        return auth.getDataAndSendComment(username, comment, password, getDataFromUser, mediaId);
    }

}
