package com.example.demo.followers;

import lombok.Data;

import java.util.List;

@Data
public class Follower {
    private int followersCount;
    private int followingCount;
    private List<Integer> allFollowers;
    private String user;
    private int mediaId;
    private int mediaCount;
    private String imageUrl;
    private List<String> validations;
}
