package com.example.demo.likes;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "like_instagram")
@Data
public class LikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @Column(name = "followers_count")
    private int followersCount;
    @Column(name = "following_count")
    private int followingCount;
    @Column(name = "followers")
    private int allFollowers;
    @Column(name = "user")
    private String user;
    @Column(name = "media_id")
    private int mediaId;
    @Column(name = "media_count")
    private int mediaCount;
    @Column(name = "image_url")
    private String imageUrl;
//    @Column(name = "validations")
//    private List<String> validations;
}
