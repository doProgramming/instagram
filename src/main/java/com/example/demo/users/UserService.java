package com.example.demo.users;

import java.util.List;

public interface UserService {
    String addUser(String username, String password, String email);
    String addUsers(List<User> users);
}
