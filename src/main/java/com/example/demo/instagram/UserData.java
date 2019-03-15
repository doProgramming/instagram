package com.example.demo.instagram;

import lombok.Data;
import org.brunocvcunha.instagram4j.requests.payload.InstagramPostCommentResult;

@Data
public class UserData {
    String userName;
    String country;
    String email;
    String phoneNumber;
    String text;
    String comment;
    InstagramPostCommentResult result;
}
