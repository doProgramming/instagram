package com.example.demo.likes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class NewFeatureLikesEndpoint {

    @Autowired
    LikesService likesService;

    @GetMapping(value = "/send/like")
    public List<Like> sendLike(@RequestParam(required = false) List<String> usernames, @RequestParam String username, @RequestParam String password, @RequestParam String getDataFromUser) throws IOException, ClassNotFoundException{
        return likesService.sendLike(username, password, getDataFromUser, usernames);
    }
//
//    @RequestMapping(value = "/send/unlike")
//    public List<Like> sendUnlike(@RequestParam(required = false) List<String> usernames, @RequestParam String username, @RequestParam String password, @RequestParam String getDataFromUser) throws IOException, ClassNotFoundException {
//        return likesService.sendUnlike(username, password, getDataFromUser, usernames);
//    }
//
    @GetMapping(value = "/send/like/all")
    public List<Like> sendLikeAll(@RequestParam(required = false) List<String> usernames, @RequestParam String username, @RequestParam String password, @RequestParam String getDataFromUser) throws IOException, ClassNotFoundException {
        return likesService.sendLlikeAll(username, password, getDataFromUser, usernames);
    }

    @PostMapping(value = "/crud/add/like")
    public Like addLike(@RequestBody Like like){
        return likesService.addLike(like);
    }

}
