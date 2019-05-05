package com.example.demo.users;

import lombok.Data;

@Data
public class User {

    private Long id;
    private String password;
    private String username;
    private String email;
    private Boolean banned;
}
