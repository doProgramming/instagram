package com.example.demo.followers;

import java.io.IOException;

public interface FollowersService {
    Follower follow(String username, String password, String getDataFromUser) throws IOException;
    Follower unfollow(String username, String password, String getDataFromUser);
}
