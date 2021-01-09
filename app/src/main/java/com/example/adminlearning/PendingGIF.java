package com.example.adminlearning;

public class PendingGIF {
    private String engCaption;
    private String malayCaption;
    private String imageUrl;
    private String date;
    private String receiver;
    private String sender;
    private String time;



    public PendingGIF() {
    }

    public PendingGIF(String engCaption, String malayCaption, String imageUrl, String date, String receiver, String sender, String time) {
        this.engCaption = engCaption;
        this.date = date;
        this.imageUrl = imageUrl;
        this.malayCaption = malayCaption;
        this.receiver = receiver;
        this.time = time;
        this.sender = sender;

    }

    public String getEngCaption() {
        return engCaption;
    }

    public void setEngCaption(String engCaption) {
        this.engCaption = engCaption;
    }

    public String getMalayCaption() {
        return malayCaption;
    }

    public void setMalayCaption(String malayCaption) {
        this.malayCaption = malayCaption;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }



}
