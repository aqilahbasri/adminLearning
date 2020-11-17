package com.example.adminlearning;

public class LearnSLlist {
    String imgurl;
    String sldescription;

    public LearnSLlist(){

    }

    public LearnSLlist(String imgurl, String sldescription) {
        this.imgurl = imgurl;
        this.sldescription = sldescription;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getSldescription() {
        return sldescription;
    }

    public void setSldescription(String sldescription) {
        this.sldescription = sldescription;
    }
}
