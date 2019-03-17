package com.example.demo.instagram;

import java.io.IOException;

public interface Auth {
    UserData getDataAndSendComment(String usernameLogin, String comment, String password, String userFrom, String mediaId, String coockiName, String coockiValue) throws IOException,ClassNotFoundException;
    UserData getData(String usernameLogin, String password, String userFrom) throws IOException,ClassNotFoundException;
    UserData oldlogin(String username,String password, String getDataFromUser) throws IOException;
    String sendComment(Authentication authentication,String coockiName, String coockiValue) throws IOException,ClassNotFoundException;
}
