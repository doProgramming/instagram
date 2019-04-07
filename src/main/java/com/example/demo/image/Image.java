package com.example.demo.image;

import lombok.Getter;
import lombok.Setter;


public class Image {
     Long id;
     String urlLink;

     public Long getId() {
          return id;
     }

     public void setId(Long id) {
          this.id = id;
     }

     public String getUrlLink() {
          return urlLink;
     }

     public void setUrlLink(String urlLink) {
          this.urlLink = urlLink;
     }
}
