package com.example.demo.instagram;

import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AuthServiceImpl implements Auth {


    @Override
    public UserData login(String username, String password, String getDataFromUser) throws IOException {
        UserData userData = new UserData();
        Instagram4j instagram = Instagram4j.builder().username(username).password(password).build();
        instagram.setup();
        instagram.login();
        String novi = getDataFromUser.replaceFirst("https://instagram.com/", "");

        InstagramSearchUsernameResult userResult = instagram.sendRequest(new InstagramSearchUsernameRequest(novi));
        userData.setStreet(userResult.getUser().address_street);
        userData.setEmail(userResult.getUser().city_name);
        userData.setUserName(userResult.getUser().username);
        userData.setEmail(userResult.getUser().public_email);
        System.out.println(username);
        System.out.println(password);
        return userData;
    }
}
