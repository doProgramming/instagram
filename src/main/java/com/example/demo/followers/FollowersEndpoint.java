package com.example.demo.followers;

import com.example.demo.likes.Like;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class FollowersEndpoint {

    @Autowired
    FollowersService followersService;

    @RequestMapping(value = "/instagram/follow")
    public Follower sendFollow(@RequestParam String username, @RequestParam String password, @RequestParam String getDataFromUser) throws IOException {
        return followersService.follow(username, password, getDataFromUser);
    }

//    @RequestMapping(value = "/instagram/unfollow")
//    public Follower sendUnfollow(@RequestParam String username, @RequestParam String password, @RequestParam String getDataFromUser) {
//        return followersService.unfollow(username, password, getDataFromUser);
//    }

}
