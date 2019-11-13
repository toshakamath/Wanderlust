package com.example.wanderlust.Doa;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class BlogObject {

    private String blogId;
    private String userId;
    private String blogTitle;
    private String blogLocation;
    private String blogText;
    private double blogLat;
    private double blogLong;
    private int blogLikes;
    private ArrayList<String> blogReviews;
    private String blogPicUrl;

    public BlogObject(@Nullable String blogId, @Nullable String userId, @Nullable String blogTitle, @Nullable String blogLocation,
                      @Nullable String blogText, @Nullable double blogLat, @Nullable double blogLong, @Nullable int blogLikes,
                      @Nullable ArrayList<String> blogReviews, @Nullable String blogPicUrl) {
        this.blogId = blogId;
        this.userId = userId;
        this.blogTitle = blogTitle;
        this.blogLocation = blogLocation;
        this.blogText = blogText;
        this.blogLat = blogLat;
        this.blogLong = blogLong;
        this.blogLikes = blogLikes;
        this.blogReviews = blogReviews;
        this.blogPicUrl = blogPicUrl;
    }

    public void setblogPicUrl(String blogPicUrl) {
        this.blogPicUrl = blogPicUrl;
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

    public String getblogPicUrl() {
        return blogPicUrl;
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

    public ArrayList<String> getBlogReviews() {
        return blogReviews;
    }

    @Override
    public String toString() {
        return "BlogObject{" +
                "blogId='" + blogId + '\'' +
                ", userId='" + userId + '\'' +
                ", blogTitle='" + blogTitle + '\'' +
                ", blogLocation='" + blogLocation + '\'' +
                ", blogText='" + blogText + '\'' +
                ", blogLat=" + blogLat +
                ", blogLong=" + blogLong +
                ", blogLikes=" + blogLikes +
                ", blogReviews=" + blogReviews +
                ", blogPicUrl='" + blogPicUrl + '\'' +
                '}';
    }
}