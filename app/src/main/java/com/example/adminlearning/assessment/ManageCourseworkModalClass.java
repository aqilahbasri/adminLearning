package com.example.adminlearning.assessment;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ManageCourseworkModalClass {

    String courseworkName;
    String courseworkQuestion;
    String courseworkFile;
    Long createdTimestamp;

    public ManageCourseworkModalClass() {

    }

    ManageCourseworkModalClass(String courseworkName, String courseworkQuestion, String courseworkFile, Long createdTimestamp) {
        this.courseworkName = courseworkName;
        this.courseworkQuestion = courseworkQuestion;
        this.courseworkFile = courseworkFile;
        this.createdTimestamp = createdTimestamp;
    }

    public String getCourseworkName() {
        return courseworkName;
    }

    public void setCourseworkName(String courseworkName) {
        this.courseworkName = courseworkName;
    }

    public String getCourseworkQuestion() {
        return courseworkQuestion;
    }

    public void setCourseworkQuestion(String courseworkQuestion) {
        this.courseworkQuestion = courseworkQuestion;
    }

    public String getCourseworkFile() {
        return courseworkFile;
    }

    public void setCourseworkFile(String courseworkFile) {
        this.courseworkFile = courseworkFile;
    }

    public Long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getDateCreated() {
        try {
            String myFormat = "dd/MM/yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
            return sdf.format(getCreatedTimestamp());
        } catch (Exception e) {
            return "date";
        }
    }
}
