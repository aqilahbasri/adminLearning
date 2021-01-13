package com.example.adminlearning.assessment;

public class OnlineInterviewApplication {

    private String name;
    private Boolean completeAssessment;
    private Boolean completeSubmission;
    private Long overallMark;
    private Long sortOrder;
    private String interviewerName;
    private String interviewDate;
    private String interviewTime;
    private String assessmentLevel;

    public OnlineInterviewApplication() {

    }

    public OnlineInterviewApplication(String assessmentLevel, Boolean completeAssessment, Boolean completeSubmission, String name,
                                      Long overallMark, Long sortOrder, String interviewerName, String interviewDate,
                                      String interviewTime) {
        this.assessmentLevel = assessmentLevel;
        this.completeAssessment = completeAssessment;
        this.completeSubmission = completeSubmission;
        this.name = name;
        this.overallMark = overallMark;
        this.sortOrder = sortOrder;
        this.interviewerName = interviewerName;
        this.interviewDate = interviewDate;
        this.interviewTime = interviewTime;
    }

    public String getAssessmentLevel() {
        return assessmentLevel;
    }

    public void setAssessmentLevel(String assessmentLevel) {
        this.assessmentLevel = assessmentLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getCompleteAssessment() {
        return completeAssessment;
    }

    public void setCompleteAssessment(Boolean completeAssessment) {
        this.completeAssessment = completeAssessment;
    }

    public Boolean getCompleteSubmission() {
        return completeSubmission;
    }

    public void setCompleteSubmission(Boolean completeSubmission) {
        this.completeSubmission = completeSubmission;
    }

    public Long getOverallMark() {
        return overallMark;
    }

//    public int getOverallMarkInteger() {
//        return overallMark.intValue();
//    }
    public void setOverallMark(Long overallMark) {
        this.overallMark = overallMark;
    }

    public Long getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Long sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getInterviewerName() {
        return interviewerName;
    }

    public void setInterviewerName(String interviewerName) {
        this.interviewerName = interviewerName;
    }

    public String getInterviewDate() {
        return interviewDate;
    }

    public void setInterviewDate(String interviewDate) {
        this.interviewDate = interviewDate;
    }

    public String getInterviewTime() {
        return interviewTime;
    }

    public void setInterviewTime(String interviewTime) {
        this.interviewTime = interviewTime;
    }
}