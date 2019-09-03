package com.example.demo.image;

import com.example.demo.Login.LoginSessionService;
import org.apache.http.client.CookieStore;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramUploadPhotoRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramConfigurePhotoResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {

    private static final Logger LOG = LoggerFactory.getLogger(ImageServiceImpl.class);

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    ImageMapper imageMapper;

    @Autowired
    LoginSessionService loginSessionService;

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
        Instagram4j instagram = loginSessionService.login(usernameHost, password);
        InstagramConfigurePhotoResult userResult = null;
        for(String imageUrl : imageUrls){
            try {
                URL url = new URL(imageUrl);
                BufferedImage img = ImageIO.read(url);
                File file = new File("downloaded.jpg");
                ImageIO.write(img, "jpg", file);
//                for (int i= 0; i<20000;i++){
//
//                }
                File image = new File("C:/Users/Public/git-doProgramming/instagram/downloaded.jpg");
                userResult = instagram.sendRequest(new InstagramUploadPhotoRequest(image, title));
            }
            catch (Exception e){
                //U slucaju da ovde uskoci "what-does-connection-reset-by-peer-mean",
                //to znaci da ova instagram4j biblioteka je skontala da je ovo bot
                //i onda odbija da saradjuje, treba samo obrisati sessiju i uuid za tog usera i radice
                //https://stackoverflow.com/questions/1434451/what-does-connection-reset-by-peer-mean
                //"Connection reset by peer" is the TCP/IP equivalent of slamming the phone back on the hook. It's more polite than merely not replying, leaving one hanging."
                LOG.error("Failed to upload Images");
            }
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
