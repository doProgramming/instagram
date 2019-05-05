package com.example.demo.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserEndpoint {

    @Autowired
    UserService userService;

    @GetMapping(value = "/addUser")
    public String addUser(@RequestParam String username, @RequestParam String password, @RequestParam String email){
        return userService.addUser(username, password, email);
    }

    @PostMapping(value = "/add/users")
    public String addUsers(@RequestBody List<User> users){
        return userService.addUsers(users);
    }
}
