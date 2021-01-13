package com.example.adminlearning.assessment;

public class GIF {
    private String engCaption;
    private String malayCaption;
    private String gifPicture;
    private String category;

    public GIF() {
    }

    public GIF(String engCaption, String malayCaption, String gifPicture, String category) {
        this.engCaption = engCaption;
        this.malayCaption = malayCaption;
        this.gifPicture = gifPicture;
        this.category = category;
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

    public String getGifPicture() {
        return gifPicture;
    }

    public void setGifPicture(String gifPicture) {
        this.gifPicture = gifPicture;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
