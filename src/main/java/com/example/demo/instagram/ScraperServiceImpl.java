package com.example.demo.instagram;

import com.example.demo.config.validnes.ValidServiceImpl;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramPostCommentRequest;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.InstagramUserFeedRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedItem;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramPostCommentResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class ScraperServiceImpl implements ScraperService {

    private static final String USER_NOT_FOUND = "User not found";
    private static final String LOGIN_REQUIRED = "login_required";
    private static final String NOT_AUTHORIZED_TO_VIEW_USER = "Not authorized to view user";
    private static final String STATUS_FAIL = "fail";


    @Override
    public Comment getDataAndSendComment(String usernameLogin, String comment, String password, String userFrom, String mediaId) throws IOException, ClassNotFoundException {

        Comment commentObj = new Comment();
        //Removes prefix and allows to be used with username and as link
        String usernameWithoutPrefix = userFrom.replaceFirst("https://instagram.com/", "");

        String mediaIdWithoutPrefix = mediaId.replaceFirst("https://www.instagram.com/p/", "");
        String mediaIdWithoutSufix = removeLastSign(mediaIdWithoutPrefix);
        //Get data from user
        InstagramSearchUsernameResult userResult = justGetUserData(usernameLogin, password, usernameWithoutPrefix);

        Instagram4j instagram = loginProxyAndCookie(usernameLogin, password);

        InstagramPostCommentResult resultFromComment = sendComment(instagram, userResult, comment, mediaIdWithoutSufix);
        if(resultFromComment!= null && resultFromComment.getComment() != null && "Active".equals(resultFromComment.getComment().getStatus())){
            commentObj.setText(resultFromComment.getComment().getText());
            commentObj.setIsSent(true);
        }
        if(STATUS_FAIL.equals(resultFromComment.getStatus())){
            commentObj.setMessage(NOT_AUTHORIZED_TO_VIEW_USER);
            return commentObj;
        }
        return commentObj;
    }

    @Override
    public UserData getData(String usernameLogin, String password, String userFrom) throws IOException, ClassNotFoundException {

        //Removes prefix and allows to be used with username and as link
        String usernameWithoutPrefix = userFrom.replaceFirst("https://instagram.com/", "");

        //Get data from user
        InstagramSearchUsernameResult userResult = justGetUserData(usernameLogin, password, usernameWithoutPrefix);

        //Map and validate user
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
    public String sendComment(Authentication authentication, String coockiName, String coockiValue) throws IOException, ClassNotFoundException {
        if (authentication == null || authentication.getUsername() == null || authentication.getUsername() == null) {
            new Exception();
        }
        //Connect to instagram account host with proxy
        Instagram4j instagram = login(authentication.getUsername(), authentication.getPassword(), coockiName, coockiValue);

        //Removes prefix and allows to be used with username and as link
        String usernameWithoutPrefix = authentication.getInstagramUser().replaceFirst("https://instagram.com/", "");

        //Get data from user
        InstagramSearchUsernameResult userResult = getUserData(authentication.getUsername(), authentication.getPassword(), usernameWithoutPrefix, coockiName, coockiValue);

        //Setup of useragent
        String useragent = "Samsung Galaxy 9";

        sendComment(instagram, userResult, authentication.getComment(), authentication.getMediaId());
        return "Comment has been sent";

    }

    /*
     *
     *
     *   1.STEP/ SETUP LOGIN WITH PROXY
     *
     *
     * */
    private Instagram4j login(String username, String password, String coockiName, String coockiValue) {

        HttpHost proxy = new HttpHost("181.177.251.7", 80, "http");
//        Instagram4j instagram =  SIngleton.getInstance(username,password,coockiName,coockiValue);
        CookieStore cookieStore = null;
        Cookie coockie = new BasicClientCookie("cookies", "true");
        ((BasicClientCookie) coockie).setSecure(true);
        ((BasicClientCookie) coockie).setAttribute("coockies", "nice");
        cookieStore.addCookie(coockie);
        Instagram4j instagram = Instagram4j.builder().username(username).password(password).cookieStore(cookieStore).userId(11648766879L).uuid("7b26602d-3722-49ee-bb6a-ba2d803ae64a").build();
        ValidServiceImpl validServiceImpl = new ValidServiceImpl();
        try {
            if (validServiceImpl.getProxyValidnes()) {
                return new Instagram4j("", "");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        cookieStore = instagram.getCookieStore();
        instagram.getProxy();
        instagram.setup();
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

    private InstagramSearchUsernameResult getUserData(String usernameHost, String password, String usernameUser, String coockiName, String coockiValue) throws IOException, ClassNotFoundException {
        Instagram4j instagram = loginProxyAndCookie(usernameHost, password);
//        Instagram4j instagram = login(usernameHost, password, coockiName, coockiValue);
        InstagramSearchUsernameResult userResult = instagram.sendRequest(new InstagramSearchUsernameRequest(usernameUser));
        return userResult;
    }

    private InstagramSearchUsernameResult justGetUserData(String usernameHost, String password, String usernameUser) throws IOException, ClassNotFoundException {
        Instagram4j instagram = loginProxyAndCookie(usernameHost, password);
//        Instagram4j instagram = login(usernameHost, password, coockiName, coockiValue);
        InstagramSearchUsernameResult userResult = instagram.sendRequest(new InstagramSearchUsernameRequest(usernameUser));
        return userResult;
    }

    private InstagramSearchUsernameResult getUserDataSendComment(String usernameHost, String password, String usernameUser, String comment) throws IOException, ClassNotFoundException {
        Instagram4j instagram = loginProxyAndCookie(usernameHost, password);
//        Instagram4j instagram = login(usernameHost, password, coockiName, coockiValue);
        InstagramSearchUsernameResult userResult = instagram.sendRequest(new InstagramSearchUsernameRequest(usernameUser));
        InstagramFeedResult tagFeed = instagram.sendRequest(new InstagramUserFeedRequest(userResult.getUser().getPk()));
        Long postId = null;
        for (InstagramFeedItem item : tagFeed.getItems()) {
            if (postId == null) {
//                if (item.getCode().equals(mediaId)) {
                if (true) {
                    postId = item.getPk();
                }
            }
        }
        InstagramPostCommentResult results = instagram.sendRequest(new InstagramPostCommentRequest(postId, comment));

        return userResult;
    }

    private static Instagram4j loginProxy(String userName, String password) {
        Instagram4j instagram = Instagram4j.builder().username(userName).password(password).build();
        instagram.setup();
        HttpHost proxy = new HttpHost("IP_SERVER", 8080, "http");
        instagram.getClient().getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        instagram.getClient().getParams().setIntParameter("http.connection.timeout", 600000);

        instagram.getClient().getCredentialsProvider().setCredentials(
                new AuthScope("IP_SERVER", 8080),
                new UsernamePasswordCredentials("0e27Df", "Z0PfAd"));
        try {
            instagram.login();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return instagram;
    }

    private Instagram4j loginProxyAndCookie(String userName, String password) throws IOException, ClassNotFoundException {
        Instagram4j instagram = Instagram4j.builder().username(userName).password(password).build();
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


    /*
     *
     *
     *    Map data from API to my wrapper class and send to user as response
     *    also call the validation
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
        return validationOfUser(userData, userResult);
    }

    //Valid if the user is Unavailable or if account is abused
    private static UserData validationOfUser(UserData userDataPredefined, InstagramSearchUsernameResult userResult){
        if (USER_NOT_FOUND.equals( userResult.getMessage())) {
            //When user is banned and not reached for some reason
            userDataPredefined.setUserName("Unavailable");
        }if(LOGIN_REQUIRED.equals(userResult.getMessage())){
            userDataPredefined.setUserName("Add delay");
        }if(NOT_AUTHORIZED_TO_VIEW_USER.equals(userResult.getMessage())){
            userDataPredefined.setUserName("Not authorized");
        }
        return userDataPredefined;
    }

    /*
     *
     *
     *   Send comment to user
     *
     *
     * */

    private InstagramPostCommentResult sendComment(Instagram4j instagram, InstagramSearchUsernameResult usernameResult, String comment, String mediaId) throws IOException {
        Boolean isCommentSent = false;
        usernameResult.getUser().getPk();
        InstagramFeedResult tagFeed = instagram.sendRequest(new InstagramUserFeedRequest(usernameResult.getUser().getPk()));
        Long postId = null;
        if(NOT_AUTHORIZED_TO_VIEW_USER.equals(tagFeed.getMessage())){
            postId = 99999L;
        }else {
            for (InstagramFeedItem item : tagFeed.getItems()) {
                if (postId == null) {
                    if (item.getCode().equals(mediaId)) {
                        postId = item.getPk();
                    }
                }
            }
        }

        InstagramPostCommentResult results = instagram.sendRequest(new InstagramPostCommentRequest(postId, comment));
        return results;
    }


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

    //Remove '/' from url
    private String removeLastSign(String url) {
        if (url != null && url.length() > 0 && url.charAt(url.length() - 1) == '/') {
            url = url.substring(0, url.length() - 1);
        }
        return url;
    }

}
