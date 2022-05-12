package edu.cuhk.csci3310.project.model;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class TutoringFavor extends Favor {
    private LatLng location;
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private String description;
    private List<String> selection;
    private Bitmap picture;
    private String courseCode;
    private int courseParticipant;

    public TutoringFavor() { super(); }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getSelection() {
        return selection;
    }

    public void setSelection(List<String> selection) {
        this.selection = selection;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public int getCourseParticipant() {
        return courseParticipant;
    }

    public void setCourseParticipant(int courseParticipant) {
        this.courseParticipant = courseParticipant;
    }
}
