package com.example.demo.jInstagram;

import com.example.demo.instagram.ProxyValid;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;

@Service
public class PersistentCookieStore implements CookieStore, Runnable, ProxyValid {
    private CookieStore store;

    public PersistentCookieStore() {
        store = new CookieManager().getCookieStore();
        // deserialize cookies into store
        //Added line
        Runtime.getRuntime().addShutdownHook(new Thread(this));
    }

    @Override
    public String setProxyValidnes() throws IOException{
        Writer fileWriter = new FileWriter("valid.txt");
        fileWriter.write("valid.");
        fileWriter.close();
        return "it is valid proxy";
    }

    @Override
    public String setGetProxyValidnes() throws IOException{
        Writer fileWriter = new FileWriter("valid.txt");
        fileWriter.write("not set valid");
        fileWriter.close();
        return "it is valid proxy";
    }

    @Override
    public Boolean getProxyValidnes() throws IOException{
        Reader fileReader = new FileReader("valid.txt");

        int data = fileReader.read();
        while(data != -1) {
            if(data==118){
                return true;
            }else {
                fileReader.close();
                return false;
            }}
        return false;
    }

    @Override
    public void run() {
        // serialize cookies to persistent storage
    }

    @Override
    public void add(URI uri, HttpCookie cookie) {
        store.add(uri, cookie);

    }

    @Override
    public List<HttpCookie> get(URI uri) {
        return store.get(uri);
    }

    @Override
    public List<HttpCookie> getCookies() {
        return store.getCookies();
    }

    @Override
    public List<URI> getURIs() {
        return store.getURIs();
    }

    @Override
    public boolean remove(URI uri, HttpCookie cookie) {
        return store.remove(uri, cookie);
    }

    @Override
    public boolean removeAll() {
        return store.removeAll();
    }

    // delegate all implementations to store object like above
}

