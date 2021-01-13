package com.example.adminlearning.assessment;

public class Level1QuestionsModel {

    String questionID;
    String question;
    String questionType;
    Long dateAdded;
    Long dateModified;
    //Do for gif also

    public Level1QuestionsModel() {}

    public Level1QuestionsModel(String questionID, String question, String questionType, Long dateAdded, Long dateModified) {
        this.questionID = questionID;
        this.question = question;
        this.questionType = questionType;
        this.dateAdded = dateAdded;
        this.dateModified = dateModified;
    }

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
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
}
