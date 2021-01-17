package com.example.adminlearning.assessment;

public class Level1QuestionsModel {

    Long dateAdded;
    Long dateModified;
    String questionID;
    String questionDetail;
    String questionType;
    String correctAnswer;
    String gifUrl;
    String engCaption, malayCaption;

    public Level1QuestionsModel() {
    }

    public Level1QuestionsModel(Long dateAdded, Long dateModified, String questionID, String questionDetail,
                                    String questionType, String correctAnswer, String gifUrl, String engCaption,
                                    String malayCaption) {
        this.dateAdded = dateAdded;
        this.dateModified = dateModified;
        this.questionID = questionID;
        this.questionDetail = questionDetail;
        this.questionType = questionType;
        this.correctAnswer = correctAnswer;
        this.gifUrl = gifUrl;
        this.engCaption = engCaption;
        this.malayCaption = malayCaption;
    }

    public Long getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Long dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Long getDateModified() {
        return dateModified;
    }

    public void setDateModified(Long dateModified) {
        this.dateModified = dateModified;
    }

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public String getQuestionDetail() {
        return questionDetail;
    }

    public void setQuestionDetail(String questionDetail) {
        this.questionDetail = questionDetail;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getGifUrl() {
        return gifUrl;
    }

    public void setGifUrl(String gifUrl) {
        this.gifUrl = gifUrl;
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
}
