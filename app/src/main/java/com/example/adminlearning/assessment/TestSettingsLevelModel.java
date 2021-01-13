package com.example.adminlearning.assessment;

public class TestSettingsLevelModel {

    String levelName, levelIconUrl;
    Long dateAdded, dateModified, duration;
    int overallPassingMark;

    public TestSettingsLevelModel() {}

    public TestSettingsLevelModel(String levelName, String levelIconUrl, Long dateAdded, Long dateModified, Long duration, int overallPassingMark) {
        this.levelName = levelName;
        this.levelIconUrl = levelIconUrl;
        this.dateAdded = dateAdded;
        this.dateModified = dateModified;
        this.duration = duration;
        this.overallPassingMark = overallPassingMark;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getLevelIconUrl() {
        return levelIconUrl;
    }

    public void setLevelIconUrl(String levelIconUrl) {
        this.levelIconUrl = levelIconUrl;
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

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public int getOverallPassingMark() {
        return overallPassingMark;
    }

    public void setOverallPassingMark(int overallPassingMark) {
        this.overallPassingMark = overallPassingMark;
    }
}
