package com.example.adminlearning.assessment;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ManageCourseworkModalClass {

    String courseworkName;
    String courseworkQuestion;
    String courseworkFile;
    Long createdTimestamp;
    Long submittedDate;
    String applicantId;
    Long courseworkMark;

    public ManageCourseworkModalClass() {

    }

    public ManageCourseworkModalClass(String courseworkName, String courseworkQuestion, String courseworkFile,
                                      Long createdTimestamp, Long submittedDate, String applicantId, Long courseworkMark) {
        this.courseworkName = courseworkName;
        this.courseworkQuestion = courseworkQuestion;
        this.courseworkFile = courseworkFile;
        this.createdTimestamp = createdTimestamp;
        this.submittedDate = submittedDate;
        this.applicantId = applicantId;
        this.courseworkMark = courseworkMark;
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

    public Long getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(Long submittedDate) {
        this.submittedDate = submittedDate;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }

    public Long getCourseworkMark() {
        return courseworkMark;
    }

    public void setCourseworkMark(Long courseworkMark) {
        this.courseworkMark = courseworkMark;
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
