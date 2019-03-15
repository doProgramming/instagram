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
        if (userResult != null && userResult.getUser() != null) {
            if (userResult.getUser().address_street != null) {
                userData.setCountry(userResult.getUser().public_phone_country_code);
            }
            if (userResult.getUser().city_name != null) {
                userData.setEmail(userResult.getUser().city_name);
            }
            if (userResult.getUser().username != null) {
                userData.setUserName(userResult.getUser().username);
            }
            if (userResult.getUser().public_email != null) {
                userData.setEmail(userResult.getUser().public_email);
            }
            if (userResult.getUser().public_phone_number != null) {
                userData.setPhoneNumber(userResult.getUser().public_phone_number);
            }
        }
        System.out.println(username);
        System.out.println(password);
        return userData;
    }
}
