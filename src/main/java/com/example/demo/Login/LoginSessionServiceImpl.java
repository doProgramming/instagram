package com.example.demo.Login;

import com.example.demo.followers.Follower;
import org.apache.http.client.CookieStore;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramGetMediaLikersRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramGetMediaLikersResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Scanner;

@Service
public class LoginSessionServiceImpl implements LoginSessionService {

    private static final Logger LOG = LoggerFactory.getLogger(LoginSessionServiceImpl.class);

    //Da bi se ulogovao moram imati Sesiju to je 7 nekih kukija i moram imati isti UUID, to je valjda random id koji treba biti isti
    // brunocvcunha commented on Oct 12, 2017 , https://github.com/brunocvcunha/instagram4j/issues/71
    @Override
    public boolean login(String username, String password) throws IOException, ClassNotFoundException {

        Instagram4j instagram = null;
        CookieStore cookieStore = null;
        String uuid = "";

        File[] directorySession = new File("user_session").listFiles();
        File [] directoryUUID = new File("user_uuid").listFiles();

        for (File cookieFile : directorySession) {
            LOG.info("Checking if there is already cookie file for current user: " + username);
            if (cookieFile.getName().contains(username) && cookieStore == null) {
                LOG.info("Yes, we have used this user before and we have cookie");
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(cookieFile));
                cookieStore = (CookieStore) ois.readObject();
                ois.close();
            }
        }for(File uuidFile : directoryUUID){
            LOG.info("Checking if there is already UUID file for current user: " + username);
            if (uuidFile.getName().contains(username) && uuid.isEmpty()){
                Scanner inFile = new Scanner(new FileReader(uuidFile));
                uuid = inFile.next();
            }
        }
        if(!uuid.isEmpty() && cookieStore != null){
            LOG.info("UUID and/or coockieStore are empty");
            instagram = Instagram4j.builder().username(username)
                    .password(password)
                    .uuid(uuid)
                    .cookieStore(cookieStore)
                    .build();

            instagram.setup();
        }else {
            LOG.info("Loging without session with username and password only");
            instagram = Instagram4j.builder()
                    .username(username)
                    .password(password)
                    .build();

            instagram.setup();
        }
        boolean isLoggedin = false;
        if (instagram != null && instagram.getRankToken() != null) {
            LOG.info("Session is valid and we didn't need to login with new session");
        }else {
            LOG.info("Session is expired or we are not able to login with this user " + username);
            for (File expiredCookieFile : directorySession){
                if(expiredCookieFile.getName().contains(username)){
                    expiredCookieFile.delete();
                    LOG.info("File: "+ expiredCookieFile + " has been deleted, as session has expired");
                }
            }
            LOG.info("Loging without session with username and password only");
             instagram = Instagram4j.builder()
                     .username(username)
                     .password(password)
                     .build();

            instagram.setup();

            try {
                if(instagram!= null && instagram.getRankToken() != null){
                    LOG.info("User has been logged in successfully");
                }else {
                    LOG.error("Failed to login for username " + username );
                }
            }catch (Exception e){
                LOG.error("Failed to login for username " + username );
            }
            isLoggedin = instagram.getRankToken() != null ? true : false;
            if(isLoggedin){
                LOG.info("Creating cookie file with session for user " + username);
                File currentUserSessionFile = new File("user_session\\" + username + ".txt");
                ObjectOutputStream sessionStream = new ObjectOutputStream(new FileOutputStream(currentUserSessionFile));
                sessionStream.writeObject(instagram.getCookieStore());
                sessionStream.flush();
                sessionStream.close();
                LOG.info("Creating uuid file with for user " + username);
                File currentUserUUIDFile = new File("user_uuid\\" + username + ".txt");
                ObjectOutputStream uuidStream = new ObjectOutputStream(new FileOutputStream(currentUserUUIDFile));
                uuidStream.writeObject(instagram.getUuid());
                uuidStream.flush();
                uuidStream.close();
            }
        }
        InstagramGetMediaLikersResult tagFeed = instagram.sendRequest(new InstagramGetMediaLikersRequest(1020304050L));

        return isLoggedin;
    }
}
