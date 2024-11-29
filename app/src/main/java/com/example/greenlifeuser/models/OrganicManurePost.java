package com.example.greenlifeuser.models;

public class OrganicManurePost {
    private String postId;
    private String imageUrl;
    private String title;
    private String desc;
    private String timestamp;
    private String number;
    private String address;
    private String name;
    private String profile;

    // No-argument constructor required for Firebase
    public OrganicManurePost() {
    }

    // Parameterized constructor for easy object creation
    public OrganicManurePost(String postId, String imageUrl, String title,  String timestamp) {
        this.postId = postId;
        this.imageUrl = imageUrl;
        this.title = title;
        this.timestamp = timestamp;
    }

    public OrganicManurePost(String postId, String title, String desc, String address, String number, String imageUrl,String name,String profile) {
        this.postId = postId;
        this.imageUrl = imageUrl;
        this.title = title;
        this.desc =desc ;
        this.timestamp = timestamp;
        this.number=number;
        this.address=address;
        this.profile=profile;
        this.name=name;


    }

    // Getters and Setters
    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDescription(String description) {
        this.desc = desc;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


    public String getAddress() {
       return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getNumber(){
        return number;
    }
    public void setNumber(String number){
        this.number=number;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){this.name=name;}
    public String getProfile(){return profile;}
    public void setProfile(String profile){this.profile=profile;}
}

