package com.example.demo.config.validnes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ConfigValidnesEndpoint {

    @Autowired
    ValidServiceImpl store;

    @GetMapping(value = "/config/server")
    public String setUp()throws IOException {
        return store.setProxyValidnes();
    }

    @GetMapping(value = "/config/server/proxy")
    public String setUpConfig()throws IOException{
        return store.setGetProxyValidnes();
    }
}
