package com.example.demo.instagram;

import com.example.demo.jInstagram.PersistentCookieStore;
import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.brunocvcunha.instagram4j.Instagram4j;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

public class SIngleton {

    public static Instagram4j instagramInstance = null;

    public SIngleton(){}

    public static Instagram4j getInstance(String coockiName, String coockivalue, String username, String password){
//        PersistentCookieStore persistentCookieStore = new PersistentCookieStore();
//        HttpGet httpget = new HttpGet("http://localhost:8070/instagram/user/data?username=zevszevs1111&password=123456sp&getDataFromUser=rowena_designst&comment=nesto");
//        persistentCookieStore.add(httpget.getURI(), persistentCookieStore.getCookies().get(0));

        Cookie cookie = new BasicClientCookie(coockiName,coockivalue);
        CookieStore cookieStore=null;
        cookieStore.addCookie(cookie);
        CookieManager cm = new CookieManager();
        CookieHandler.setDefault(cm);
        cm.getCookieStore();
        cm.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        if(instagramInstance==null){
            instagramInstance = Instagram4j.builder().username(username).password(password).cookieStore(cookieStore).userId(11648766879L).uuid("7b26602d-3722-49ee-bb6a-ba2d803ae64a").build();
        }
        return instagramInstance;
    }
}
