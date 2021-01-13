package com.example.adminlearning.assessment;

public class NewApplication {

    private String name;
    private String mark;
    private String completePresentation;

    public NewApplication() {

    }

    public NewApplication(String name, String mark, String completePresentation) {
        this.name = name;
        this.mark = mark;
        this.completePresentation = completePresentation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getCompletePresentation() {
        return completePresentation;
    }

    public void setCompletePresentation(String completePresentation) {
        this.completePresentation = completePresentation;
    }

}
