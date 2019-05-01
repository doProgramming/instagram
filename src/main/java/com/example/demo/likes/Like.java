package com.example.demo.likes;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Like {

    private Long id;
    private int followersCount;
    private int followingCount;
    private int allFollowers;
    private String user;
    private int mediaId;
    private int mediaCount;
    private String imageUrl;
    private List<String> validations;
}
