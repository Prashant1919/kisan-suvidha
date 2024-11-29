package com.example.greenlifeuser.models;

public class RecycleModel {

    private String rid, title, image, subTitle1, description, subTitle2, link1, link2, link3;

    public RecycleModel() {
    }

    public RecycleModel(String rid, String title, String image, String subTitle1, String description, String subTitle2, String link1, String link2, String link3) {
        this.rid = rid;
        this.title = title;
        this.image = image;
        this.subTitle1 = subTitle1;
        this.description = description;
        this.subTitle2 = subTitle2;
        this.link1 = link1;
        this.link2 = link2;
        this.link3 = link3;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSubTitle1() {
        return subTitle1;
    }

    public void setSubTitle1(String subTitle1) {
        this.subTitle1 = subTitle1;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubTitle2() {
        return subTitle2;
    }

    public void setSubTitle2(String subTitle2) {
        this.subTitle2 = subTitle2;
    }

    public String getLink1() {
        return link1;
    }

    public void setLink1(String link1) {
        this.link1 = link1;
    }

    public String getLink2() {
        return link2;
    }

    public void setLink2(String link2) {
        this.link2 = link2;
    }

    public String getLink3() {
        return link3;
    }

    public void setLink3(String link3) {
        this.link3 = link3;
    }
}
