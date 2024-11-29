package com.example.greenlifeuser.models;

public class ContactUsModel {
    String contactName, contactEmail, contactMessage;

    public ContactUsModel() {
    }

    public ContactUsModel(String contactName, String contactEmail, String contactMessage) {
        this.contactName = contactName;
        this.contactEmail = contactEmail;
        this.contactMessage = contactMessage;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactMessage() {
        return contactMessage;
    }

    public void setContactMessage(String contactMessage) {
        this.contactMessage = contactMessage;
    }
}
