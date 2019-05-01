package com.example.demo.likes;

import java.io.IOException;
import java.util.List;

public interface LikesService {

    List<Like> sendLike(String username, String password, String getDataFromUser, List<String> usernames) throws IOException, ClassNotFoundException;
    List<Like> sendUnlike(String username, String password, String getDataFromUser,List<String> usernames) throws IOException, ClassNotFoundException;
    List<Like> sendLlikeAll(String username, String password, String getDataFromUser, List<String> usernames) throws IOException, ClassNotFoundException;
    Like addLike(Like like);
}
