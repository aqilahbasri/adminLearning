package com.example.adminlearning;

public class UploadCategory {
    public String categoryimage;
    public String categoryname;

    public UploadCategory(){}

    public UploadCategory(String name, String url) {
        this.categoryimage = name;
        this.categoryname = url;
    }

    public String getCategoryname() {
        return categoryname;
    }
    public String getCategoryimage() {
        return categoryimage;
    }

}
