package com.example.demo.instagram;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.conn.params.ConnRoutePNames;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramPostCommentRequest;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.InstagramUserFeedRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedItem;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramPostCommentResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AuthServiceImpl implements Auth {


    @Override
    public UserData getDataAndSendComment(String usernameLogin, String comment, String password, String userFrom, String mediaId) throws IOException {

        //Connect to instagram account host with proxy
        Instagram4j instagram = login(usernameLogin, password);

        //Removes prefix and allows to be used with username and as link
        String usernameWithoutPrefix = userFrom.replaceFirst("https://instagram.com/", "");

        //Get data from user
        InstagramSearchUsernameResult userResult = getUserData(usernameLogin, password, usernameWithoutPrefix);

        //Setup of useragent
        String useragent = "Samsung Galaxy 9";

        sendComment(instagram, userResult, comment, mediaId);
        return mapFromInstagramToUserData(userResult);
    }

    /*
     *
     *
     *    Old login without proxy
     *
     *
     * */

    @Override
    public UserData oldlogin(String username, String password, String getDataFromUser) throws IOException {
        Instagram4j instagram = Instagram4j.builder().username(username).password(password).build();
        instagram.setup();
        instagram.login();

        //Removes prefix and allows to be used with username and as link
        String usernameWithoutPrefix = getDataFromUser.replaceFirst("https://instagram.com/", "");

        InstagramSearchUsernameResult userResult = instagram.sendRequest(new InstagramSearchUsernameRequest(usernameWithoutPrefix));

        return mapFromInstagramToUserData(userResult);
    }

    @Override
    public String sendComment(Authentication authentication) throws IOException {
        if (authentication == null || authentication.getUsername() == null || authentication.getUsername() == null) {
            new Exception();
        }
        //Connect to instagram account host with proxy
        Instagram4j instagram = login(authentication.getUsername(), authentication.getPassword());

        //Removes prefix and allows to be used with username and as link
        String usernameWithoutPrefix = authentication.getInstagramUser().replaceFirst("https://instagram.com/", "");

        //Get data from user
        InstagramSearchUsernameResult userResult = getUserData(authentication.getUsername(), authentication.getPassword(), usernameWithoutPrefix);

        //Setup of useragent
        String useragent = "Samsung Galaxy 9";

        sendComment(instagram, userResult, authentication.getComment(),authentication.getMediaId());
        return "Comment has been sent";

    }

    /*
     *
     *
     *   1.STEP/ SETUP LOGIN WITH PROXY
     *
     *
     * */
    private Instagram4j login(String username, String password) {
        Instagram4j instagram = Instagram4j.builder().username(username).password(password).build();
        instagram.setup();
        HttpHost proxy = new HttpHost("107.152.153.235", 9788, "http");
        instagram.getClient().getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        instagram.getClient().getParams().setIntParameter("http.connection.timeout", 600000);

        instagram.getClient().getCredentialsProvider().setCredentials(
                new AuthScope("107.152.153.235", 9788),
                new UsernamePasswordCredentials("0e27Df", "Z0PfAd"));
        try {
            instagram.login();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return instagram;
    }

    /*
     *
     *
     *    WIth un and pw login and get DATA of the user
     *
     *
     * */

    private InstagramSearchUsernameResult getUserData(String usernameHost, String password, String usernameUser) throws IOException {
        Instagram4j instagram = login(usernameHost, password);
        InstagramSearchUsernameResult userResult = instagram.sendRequest(new InstagramSearchUsernameRequest(usernameUser));
        return userResult;
    }


    /*
     *
     *
     *    Map data from API to my wrapper class and send to user as response
     *
     *
     * */

    private UserData mapFromInstagramToUserData(InstagramSearchUsernameResult userResult) {
        UserData userData = new UserData();
        if (userResult != null && userResult.getUser() != null) {
            if (userResult.getUser().getZip() != null) {
                userData.setText(userResult.getUser().getZip());
            }
            if (userResult.getUser().public_phone_country_code != null) {
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
        return userData;
    }

    /*
     *
     *
     *   Send comment to user
     *
     *
     * */

    private InstagramPostCommentResult sendComment(Instagram4j instagram, InstagramSearchUsernameResult usernameResult, String comment, String mediaId) throws IOException {
        usernameResult.getUser().getPk();
        InstagramFeedResult tagFeed = instagram.sendRequest(new InstagramUserFeedRequest(usernameResult.getUser().getPk()));
        Long postId = null;
        for (InstagramFeedItem item : tagFeed.getItems()) {
            if (postId == null) {
                if (item.getCode().equals(mediaId)) {
                    postId = item.getPk();
                }
            }
        }
//                tagFeed.getItems().get(0).getPk();

        //Check is this getUser.getPk() I need id of user
        //InstagramFeedResult feedResult = instagram.sendRequest(new InstagramUserFeedRequest(postId));

        InstagramPostCommentResult results = instagram.sendRequest(new InstagramPostCommentRequest(postId, comment));
        return results;
    }

//    getFeedFromHashTag
//    InstagramFeedResult tagFeed = instagram.sendRequest(new InstagramTagFeedRequest("github"));
//    for (InstagramFeedItem feedResult : tagFeed.getItems()) {
//        System.out.println("Post ID: " + feedResult.getPk());
//    }
}
