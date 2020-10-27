package com.example.adminlearning;

public class UploadCategory {
    public String categoryname;
    public String categoryimage;


    public UploadCategory(){}

    public UploadCategory(String url, String name) {
        this.categoryname = name;
        this.categoryimage = url;

    }

    public String getCategoryname() {
        return categoryname;
    }
    public String getCategoryimage() {
        return categoryimage;
    }

}
