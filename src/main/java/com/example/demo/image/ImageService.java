package com.example.demo.image;

import org.brunocvcunha.instagram4j.requests.payload.InstagramConfigurePhotoResult;

import java.io.IOException;

public interface ImageService {
    InstagramConfigurePhotoResult uploadImage(String username, String password, String imageUrl, String title) throws IOException, ClassNotFoundException;
    Image saveImage(Image image);
}
