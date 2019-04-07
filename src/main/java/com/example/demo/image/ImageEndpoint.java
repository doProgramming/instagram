package com.example.demo.image;

import org.brunocvcunha.instagram4j.requests.payload.InstagramConfigurePhotoResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class ImageEndpoint {

    @Autowired
    ImageService imageService;

    @GetMapping(value = "/instagram/postImage")
    public InstagramConfigurePhotoResult setImage(@RequestParam String username, @RequestParam String password, @RequestParam String urlImage, @RequestParam String title) throws IOException, ClassNotFoundException {
        return imageService.uploadImage(username, password, urlImage, title);
    }

    @PostMapping(value = "/instagram/save")
    public Image saveImage(@RequestBody Image image){
        return imageService.saveImage(image);
    }
}
