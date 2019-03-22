package com.example.demo.instagram;

import lombok.Data;

@Data
public class Comment {
    private String text;
    private Boolean isSent;
    private String message;
}
