package com.example.demo.mera;

import java.util.List;

public interface UserService {

    List<UserEntity> getUsersForProvidedRole(String roleName);
}
