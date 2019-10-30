package com.example.wanderlust.Doa;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class BlogObject {

    private String blogId;
    private String userId;
    private String blogTitle;
    private String blogLocation;
    private String blogText;
    private ArrayList<byte[]> blogPics;
    private double blogLat;
    private double blogLong;
    private int blogLikes;
    private  String blogReviews;

    public BlogObject(@Nullable String blogId, @Nullable String userId, @Nullable String blogTitle, @Nullable String blogLocation,
                      @Nullable String blogText, @Nullable ArrayList<byte[]> blogPics, @Nullable double blogLat, @Nullable double blogLong, @Nullable int blogLikes,
                      @Nullable String blogReviews) {
        this.blogId = blogId;
        this.userId = userId;
        this.blogTitle = blogTitle;
        this.blogLocation = blogLocation;
        this.blogText = blogText;
        this.blogPics = blogPics;
        this.blogLat = blogLat;
        this.blogLong = blogLong;
        this.blogLikes = blogLikes;
        this.blogReviews = blogReviews;
    }

    public void setBlogId(String blogId) {
        this.blogId = blogId;
    }

    public String getBlogId() {
        return blogId;
    }

    public String getUserId() {
        return userId;
    }

    public String getBlogTitle() {
        return blogTitle;
    }

    public String getBlogLocation() {
        return blogLocation;
    }

    public String getBlogText() {
        return blogText;
    }

    public ArrayList<byte[]> getBlogPics() {
        return blogPics;
    }

    public double getBlogLat() {
        return blogLat;
    }

    public double getBlogLong() {
        return blogLong;
    }

    public int getBlogLikes() {
        return blogLikes;
    }

    public String getBlogReviews() {
        return blogReviews;
    }
}
