package com.example.adminlearning.assessment;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ApplyCertContactInfo {

    private String address, city, postcode, phoneNumber, state;
    private String name;
    private Long appliedTimestamp;
    private Long approvedTimestamp;

    ApplyCertContactInfo() {    //Required empty constructor

    }

    public ApplyCertContactInfo(String name, String address, String city, String postcode, String phoneNumber, String state) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.postcode = postcode;
        this.phoneNumber = phoneNumber;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getAppliedTimestamp() {
        return appliedTimestamp;
    }

    public void setAppliedTimestamp(Long appliedTimestamp) {
        this.appliedTimestamp = appliedTimestamp;
    }

    public Long getApprovedTimestamp() {
        return approvedTimestamp;
    }

    public void setApprovedTimestamp(Long appliedTimestamp) {
        this.approvedTimestamp = appliedTimestamp;
    }

    public String getTimeDate(){
        try{
            String myFormat = "dd/MM/yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
            return sdf.format(getAppliedTimestamp());
        } catch(Exception e) {
            return "date";
        }

//        String timeFormat = "hh:mm aa";
//        SimpleDateFormat sdf2 = new SimpleDateFormat(timeFormat, Locale.UK);
//        interviewTime.setText(sdf2.format(calendar.getTime()));
    }

    public String getApprovedDate() {
        try{
            String myFormat = "dd/MM/yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
            return sdf.format(getApprovedTimestamp());
        } catch(Exception e) {
            return "date";
        }
    }
}