package com.example.demo.instagram;

import com.example.demo.jInstagram.PersistentCookieStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class AuthServiceEndpoint {

    @Autowired
    Auth auth;

    @Autowired
    PersistentCookieStore store;

    @GetMapping(value = "/instagram/user/data/withproxy")
    public UserData getDataFromInstagram(@RequestHeader String coockiName,@RequestHeader String coockiValue,  @RequestParam String username, @RequestParam String password
            , @RequestParam String comment, @RequestParam String getDataFromUser, @RequestParam String mediaId) throws IOException,ClassNotFoundException {
        return auth.getDataAndSendComment(username, comment, password, getDataFromUser, mediaId, coockiName, coockiValue);
    }

    @GetMapping(value = "/instagram/getdata")
    public UserData getData(@RequestParam String username, @RequestParam String password
            ,  @RequestParam String getDataFromUser) throws IOException,ClassNotFoundException {
        return auth.getData(username, password, getDataFromUser);
    }

    @GetMapping(value = "/instagram/user/old/data/withoutproxy")
    public UserData getDataFromInstagram(@RequestHeader String coockiName,@RequestHeader String coockiValue,@RequestParam String username, @RequestParam String password, @RequestParam String getDataFromUser) throws IOException {
        return auth.oldlogin(username, password, getDataFromUser);
    }

    @PostMapping(value = "/instagram/sendComment")
    public String sendCommentSecured(@RequestHeader String coockiName,@RequestHeader String coockiValue,@RequestBody Authentication authentication) throws IOException, ClassNotFoundException {
        return auth.sendComment(authentication, coockiName, coockiValue);
    }

    @GetMapping(value = "/instagram/sendComment")
    public UserData sendComment(@RequestHeader String coockiName,@RequestHeader String coockiValue,@RequestParam String username, @RequestParam String password
            , @RequestParam String comment, @RequestParam String getDataFromUser,@RequestParam String mediaId) throws IOException,ClassNotFoundException {
        return auth.getDataAndSendComment(username, comment, password, getDataFromUser, mediaId, coockiName, coockiValue);
    }

    @GetMapping(value = "/config/server")
    public String setUp()throws IOException{
       return store.setProxyValidnes();
    }

}
