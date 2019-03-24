package com.example.demo.instagram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class ScraperEndpoint {

    @Autowired
    ScraperService scraperService;

//    @GetMapping(value = "/instagram/user/data/withproxy")
//    public Comment getDataFromInstagram(@RequestParam String username, @RequestParam String password
//            , @RequestParam String comment, @RequestParam String getDataFromUser, @RequestParam String mediaId) throws IOException,ClassNotFoundException {
//        return scraperService.sendCommentGiveResponse(username, comment, password, getDataFromUser, mediaId);
//    }

    @GetMapping(value = "/instagram/getdata/noproxy")
    public UserData getDataNoProxy(@RequestParam String username, @RequestParam String password
            ,  @RequestParam String getDataFromUser) throws IOException,ClassNotFoundException {
        return scraperService.getDataNoProxy(username, password, getDataFromUser);
    }

    @GetMapping(value = "/instagram/getdata")
    public UserData getData(@RequestParam String username, @RequestParam String password
            ,  @RequestParam String getDataFromUser) throws IOException,ClassNotFoundException {
        return scraperService.getData(username, password, getDataFromUser);
    }

    @GetMapping(value = "/instagram/getdata/withoutproxy")
    public UserData getDataFromInstagram(@RequestParam String username, @RequestParam String password, @RequestParam String getDataFromUser) throws IOException {
        return scraperService.oldlogin(username, password, getDataFromUser);
    }

//    @PostMapping(value = "/instagram/sendComment")
//    public String sendCommentSecured(@RequestHeader String coockiName,@RequestHeader String coockiValue,@RequestBody Authentication authentication) throws IOException, ClassNotFoundException {
//        return scraperService.sendComment(authentication, coockiName, coockiValue);
//    }

    @GetMapping(value = "/instagram/sendComment")
    public Comment sendComment(@RequestParam String username, @RequestParam String password
            , @RequestParam String comment, @RequestParam String getDataFromUser,@RequestParam String mediaId) throws IOException,ClassNotFoundException {
        return scraperService.sendCommentGiveResponse(username, comment, password, getDataFromUser, mediaId);
    }

}
