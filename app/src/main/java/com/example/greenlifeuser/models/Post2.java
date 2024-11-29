package com.example.greenlifeuser.models;

import com.google.firebase.database.ServerValue;

public class Post2 {

    private String postKey;
    private String title;
    private String description;
    private String source;
    private String price;
    private String number;
    private String picture;
    private String userId;
    private String userPhoto;
    private String userName;
    private Object timeStamp ;

    public Post2(String title, String description, String source, String price, String number, String picture, String userId, String userPhoto, String userName) {
        this.title = title;
        this.description = description;
        this.source = source;
        this.price = price;
        this.number = number;
        this.picture = picture;
        this.userId = userId;
        this.userPhoto = userPhoto;
        this.userName = userName;
        this.timeStamp = ServerValue.TIMESTAMP;
    }

    public Post2() {
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSource() {
        return source;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
