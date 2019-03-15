package com.example.demo.instagram;

import java.io.IOException;

public interface Auth {
    UserData getDataAndSendComment(String username, String comment, String password, String getDataFromUser) throws IOException;
    UserData oldlogin(String username,String password, String getDataFromUser) throws IOException;
    String sendComment(Authentication authentication) throws IOException;
}
