package com.example.demo.image;

import org.apache.http.client.CookieStore;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramUploadPhotoRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramConfigurePhotoResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    ImageMapper imageMapper;

    @Override
    public InstagramConfigurePhotoResult uploadImage(String username, String password, String imageUrl, String title) throws IOException, ClassNotFoundException {
        InstagramConfigurePhotoResult userResult = justGetUserData(username, password, imageUrl, title);
        return userResult;
    }

    @Override
    public InstagramConfigurePhotoResult uploadImages(String username, String password, List<String> imageUrls, String title) throws IOException, ClassNotFoundException {
        InstagramConfigurePhotoResult userResult = uploadAllImages(username, password, imageUrls, title);
        return userResult;
    }

    @Override
    public Image saveImage(Image image) {
        ImageEntity imageEntity = imageMapper.mapToEntity(image);
        imageEntity.setUrlLink(image.getUrlLink());
        imageEntity = imageRepository.save(imageEntity);
        Image imageModel = imageMapper.mapToModel(imageEntity);
        return imageModel;
    }

    private InstagramConfigurePhotoResult justGetUserData(String usernameHost, String password, String imageUrl, String title) throws IOException, ClassNotFoundException {
        Instagram4j instagram = loginProxyAndCookie(usernameHost, password);
        URL url = new URL(imageUrl);
        BufferedImage img = ImageIO.read(url);
        File file = new File("downloaded.jpg");
        ImageIO.write(img, "jpg", file);
        InstagramConfigurePhotoResult userResult =instagram.sendRequest(new InstagramUploadPhotoRequest(new File("C:/Cmder/instagram/downloaded.jpg"), title));
        return userResult;
    }

    private InstagramConfigurePhotoResult uploadAllImages(String usernameHost, String password, List<String> imageUrls, String title) throws IOException, ClassNotFoundException {
        Instagram4j instagram = loginProxyAndCookie(usernameHost, password);
        InstagramConfigurePhotoResult userResult = null;
        for(String imageUrl : imageUrls){
            try {
                URL url = new URL(imageUrl);
                BufferedImage img = ImageIO.read(url);
                File file = new File("downloaded.jpg");
                ImageIO.write(img, "jpg", file);
                userResult =instagram.sendRequest(new InstagramUploadPhotoRequest(new File("C:/Cmder/instagram/downloaded.jpg"), title));
            }catch (Exception e){}
        }return userResult;
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
}
