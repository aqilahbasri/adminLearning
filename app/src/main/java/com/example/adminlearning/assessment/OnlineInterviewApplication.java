package com.example.adminlearning.assessment;

public class OnlineInterviewApplication {

    private String name;
    private Boolean completeAssessment;
    private Boolean completeSubmission;
    private Long overallMark;
    private Long sortOrder;
    private String interviewerName;
    private String assessmentLevel;
    private String userId;
    private Long interviewTime;
    private String interviewerId;

    public OnlineInterviewApplication() {

    }

    public OnlineInterviewApplication(String assessmentLevel, Boolean completeAssessment, Boolean completeSubmission, String name,
                                      Long overallMark, Long sortOrder, String interviewerName, String userId, Long interviewTime,
                                      String interviewerId) {
        this.assessmentLevel = assessmentLevel;
        this.completeAssessment = completeAssessment;
        this.completeSubmission = completeSubmission;
        this.name = name;
        this.overallMark = overallMark;
        this.sortOrder = sortOrder;
        this.interviewerName = interviewerName;
        this.interviewTime = interviewTime;
        this.userId = userId;
        this.interviewerId = interviewerId;
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

    public Long getInterviewTime() {
        return interviewTime;
    }

    public void setInterviewTime(Long interviewTime) {
        this.interviewTime = interviewTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getInterviewerId() {
        return interviewerId;
    }

    public void setInterviewerId(String interviewerId) {
        this.interviewerId = interviewerId;
    }
}