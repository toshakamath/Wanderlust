package com.example.wanderlust.Doa;

import java.util.ArrayList;

public class UserObject {

    private String userId;
    private String name;
    private String email;
    private String password;
    private ArrayList<String> bookmarkIds;

    public UserObject(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    public UserObject(String userId, String name, String email, String password, ArrayList<String> bookmarkIds) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.bookmarkIds = bookmarkIds;
    }

    public UserObject(String userId, String name, String email, String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<String> getBookmarkIds() {
        return bookmarkIds;
    }

    public void setBookmarkIds(ArrayList<String> bookmarkIds) {
        this.bookmarkIds = bookmarkIds;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserObject{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
