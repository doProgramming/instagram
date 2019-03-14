package com.example.demo.instagram;

import java.io.IOException;

public interface Auth {
    UserData login(String username, String password, String getDataFromUser) throws IOException;
}
