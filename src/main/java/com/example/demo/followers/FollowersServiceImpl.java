package com.example.demo.followers;

import com.example.demo.config.validnes.ValidServiceImpl;
import org.apache.http.client.CookieStore;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramFollowRequest;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.InstagramUnfollowRequest;
import org.brunocvcunha.instagram4j.requests.InstagramUserFeedRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class FollowersServiceImpl implements FollowersService {

    @Override
    public Follower follow(String username, String password, String getDataFromUser) throws IOException {

        Follower follower = new Follower();
        Instagram4j instagram = null;
        InstagramSearchUsernameResult userResult = null;
        InstagramFeedResult tagFeed = null;
        try {
            instagram = loginProxyAndCookie(username,password);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            userResult = instagram.sendRequest(new InstagramSearchUsernameRequest(getDataFromUser));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(userResult!= null && userResult.getUser()!= null){
            tagFeed = instagram.sendRequest(new InstagramUserFeedRequest(userResult.getUser().getPk()));
            follower.setFollowersCount(userResult.getUser().getFollower_count());
            follower.setFollowingCount(userResult.getUser().getFollowing_count());
            follower.setMediaCount(userResult.getUser().getMedia_count());
            follower.setImageUrl(userResult.getUser().getProfile_pic_url());
        }
        if(tagFeed != null && !tagFeed.getItems().isEmpty()){
            follower.setMediaId(tagFeed.getItems().get(0).getMedia_type());
            instagram.sendRequest(new InstagramFollowRequest(userResult.getUser().getPk()));
        }else {
            List<String> validations = new ArrayList<>();
            validations.add("Request to follow is sent");
            follower.setValidations(validations);
        }
        return follower;
    }

    private Instagram4j loginProxyAndCookie(String userName, String password) throws IOException,ClassNotFoundException {
        Instagram4j instagram = Instagram4j.builder().username(userName).password(password).build();
        //

        ValidServiceImpl validServiceImpl = new ValidServiceImpl();
        try {
            if(validServiceImpl.getProxyValidnes()){
                return new Instagram4j("","");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        instagram.setup();
        instagram.login();
        File cookieLogin = new File(userName + ".txt");

        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(cookieLogin));
        oos.writeObject(instagram.getCookieStore());
        oos.close();

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(cookieLogin));
        CookieStore cookieStore = (CookieStore) ois.readObject();
        ois.close();

        Instagram4j instagram2 = Instagram4j.builder().username(userName)
                .password(password)
                .uuid(instagram.getUuid())
                .cookieStore(cookieStore)
                .build();
        instagram2.setup();
        return instagram2;
    }

    @Override
    public Follower unfollow(String username, String password, String getDataFromUser) {

        Follower follower = new Follower();
        Instagram4j instagram = null;
        InstagramSearchUsernameResult userResult = null;
        InstagramFeedResult tagFeed = null;
        try {
            instagram = loginProxyAndCookie(username,password);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            userResult = instagram.sendRequest(new InstagramSearchUsernameRequest(getDataFromUser));
            tagFeed = instagram.sendRequest(new InstagramUserFeedRequest(userResult.getUser().getPk()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(userResult!= null && userResult.getUser()!= null){
            follower.setFollowersCount(userResult.getUser().getFollower_count());
            follower.setFollowingCount(userResult.getUser().getFollowing_count());
            follower.setMediaCount(userResult.getUser().getMedia_count());
            follower.setImageUrl(userResult.getUser().getProfile_pic_url());
        }
        follower.setMediaId(tagFeed.getItems().get(0).getMedia_type());
        try {
            instagram.sendRequest(new InstagramUnfollowRequest(tagFeed.getItems().get(0).getPk()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return follower;
    }
}
