package com.example.demo.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    @Override
    public String addUser(String username, String password, String email) {
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setBanned(false);
        users.add(user);
        return addAllUsers(users);
    }

    @Override
    public String addUsers(List<User> users) {
        return addAllUsers(users);
    }

    private String addAllUsers(List<User> users) {
        UserEntity user = new UserEntity();
        int unSavedUsers = 0;
        int savedUsers = 0;
        for (User oneUser : users) {
            user.setUsername(oneUser.getUsername());
            user.setPassword(oneUser.getPassword());
            user.setEmail(oneUser.getEmail());
            userRepository.save(user);
            if (user != null) {
                if (users.size() == 1 && user.getId() == null) {
                    return "User is not saved";
                }
            }
            if (user.getId() == null) {
                unSavedUsers++;
            } else {
                savedUsers++;
            }
        }

        return "Users that are saved: " + savedUsers + ". Users that are not saved: " + unSavedUsers;
    }
}
