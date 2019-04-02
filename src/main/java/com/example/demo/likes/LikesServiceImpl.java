package com.example.demo.likes;

import com.example.demo.config.validnes.ValidServiceImpl;
import org.apache.http.client.CookieStore;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramLikeRequest;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.InstagramUnlikeRequest;
import org.brunocvcunha.instagram4j.requests.InstagramUserFeedRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedItem;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class LikesServiceImpl implements LikesService {

    @Override
    public List<Like> sendLike(String username, String password, String getDataFromUser, List<String> usernames) throws IOException, ClassNotFoundException {

        List<Like> likes = new ArrayList<>();
        Like like = new Like();
        Instagram4j instagram = null;
        InstagramSearchUsernameResult userResult = null;
        InstagramFeedResult tagFeed = null;
        String validation = null;
        if (usernames == null || usernames.isEmpty()) {
            usernames.add(username);
        }
        for (String usernameFromList : usernames) {
            username = usernameFromList;

            instagram = loginProxyAndCookie(username, password);

            userResult = instagram.sendRequest(new InstagramSearchUsernameRequest(getDataFromUser));
            if(userResult!=null&&userResult.getUser()!=null){
                tagFeed = instagram.sendRequest(new InstagramUserFeedRequest(userResult.getUser().getPk()));
            }

            if (userResult != null && userResult.getUser() != null) {
                like.setUser(userResult.getUser().getUsername());
                like.setFollowersCount(userResult.getUser().getFollower_count());
                like.setFollowingCount(userResult.getUser().getFollowing_count());
                like.setMediaCount(userResult.getUser().getMedia_count());
                like.setImageUrl(userResult.getUser().getProfile_pic_url());
            }
            //If user is private it will not allowed use to get data so here we set that Account is private
            if (tagFeed == null || tagFeed.getItems() == null) {
                List<String> validations = new ArrayList<>();
                validation = "Account: " + like.getUser() + " is Private";
                validations.add(validation);
                like.setValidations(validations);
            }
            //If user is not private or we are already in his list of followers
            if (validation == null) {
                like.setMediaId(tagFeed.getItems().get(0).getMedia_type());
                instagram.sendRequest(new InstagramLikeRequest(tagFeed.getItems().get(0).getPk()));
            }
            likes.add(like);
        }
        return likes;
    }

    @Override
    public List<Like> sendUnlike(String username, String password, String getDataFromUser, List<String> usernames) throws IOException, ClassNotFoundException {

        List<Like> likes = new ArrayList<>();
        Like like = new Like();
        Instagram4j instagram = null;
        InstagramSearchUsernameResult userResult = null;
        InstagramFeedResult tagFeed = null;

        if (usernames == null || usernames.isEmpty()) {
            usernames.add(username);
        }
        for (String usernameFromList : usernames) {
            username = usernameFromList;


            instagram = loginProxyAndCookie(username, password);


            userResult = instagram.sendRequest(new InstagramSearchUsernameRequest(getDataFromUser));
            tagFeed = instagram.sendRequest(new InstagramUserFeedRequest(userResult.getUser().getPk()));

            if (userResult != null && userResult.getUser() != null) {
                like.setFollowersCount(userResult.getUser().getFollower_count());
                like.setFollowingCount(userResult.getUser().getFollowing_count());
                like.setMediaCount(userResult.getUser().getMedia_count());
                like.setImageUrl(userResult.getUser().getProfile_pic_url());
            }
            like.setMediaId(tagFeed.getItems().get(0).getMedia_type());
            instagram.sendRequest(new InstagramUnlikeRequest(tagFeed.getItems().get(0).getPk()));
            likes.add(like); }
        return likes;
    }

    @Override
    public List<Like> sendLlikeAll(String username, String password, String getDataFromUser, List<String> usernames) throws IOException, ClassNotFoundException {

        List<Like> likes = new ArrayList<>();
        Like like = new Like();
        Instagram4j instagram = null;
        InstagramSearchUsernameResult userResult = null;
        InstagramFeedResult tagFeed = null;

        if (usernames == null || usernames.isEmpty()) {
            usernames.add(username);
        }
        for (String usernameFromList : usernames) {
            username = usernameFromList;

        instagram = loginProxyAndCookie(username, password);
        userResult = instagram.sendRequest(new InstagramSearchUsernameRequest(getDataFromUser));
        tagFeed = instagram.sendRequest(new InstagramUserFeedRequest(userResult.getUser().getPk()));

        if (userResult != null && userResult.getUser() != null) {
            like.setFollowersCount(userResult.getUser().getFollower_count());
            like.setFollowingCount(userResult.getUser().getFollowing_count());
            like.setMediaCount(userResult.getUser().getMedia_count());
            like.setImageUrl(userResult.getUser().getProfile_pic_url());
        }
        like.setMediaId(tagFeed.getItems().get(0).getMedia_type());

        for (InstagramFeedItem item : tagFeed.getItems()) {
            instagram.sendRequest(new InstagramLikeRequest(item.getPk()));
        }
        likes.add(like);}

        return likes;
    }

    private Instagram4j loginProxyAndCookie(String userName, String password) throws IOException, ClassNotFoundException {
        Instagram4j instagram = Instagram4j.builder().username(userName).password(password).build();
        //

        ValidServiceImpl validServiceImpl = new ValidServiceImpl();
        try {
            if (validServiceImpl.getProxyValidnes()) {
                return new Instagram4j("", "");
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
}
