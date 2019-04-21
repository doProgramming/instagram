package com.example.demo.bot;

import com.example.demo.email.EmailService;
import com.example.demo.image.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BotServiceImpl implements BotService {

    @Autowired
    EmailService emailService;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    ImageMapper imageMapper;

    @Override
    public void sendGroupOfEmails(String message) {
        List<ImageEntity> entities = imageRepository.findAll();
        Image image;
        String url;
        for (ImageEntity entity : entities) {
            image = imageMapper.mapToModel(entity);
            if (image != null && image.getUrlLink() != null) {
                url = image.getUrlLink();
                emailService.sendEmail(
                        "Subject",
                        "Hello, we are company from Lisabon and we would like to...",
                        "Dear Roberto", "Toptal enginnering", url);
            }
        }
    }
}
