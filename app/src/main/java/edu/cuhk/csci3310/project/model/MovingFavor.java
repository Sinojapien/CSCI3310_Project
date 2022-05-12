package edu.cuhk.csci3310.project.model;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

public class MovingFavor extends Favor {
    LatLng startLoc;
    LatLng endLoc;
    String date;
    String time;
    String description;
    Bitmap photo;

    public MovingFavor() {
        super();
    }

    public LatLng getStartLoc() {
        return startLoc;
    }

    public void setStartLoc(LatLng startLoc) {
        this.startLoc = startLoc;
    }

    public LatLng getEndLoc() {
        return endLoc;
    }

    public void setEndLoc(LatLng endLoc) {
        this.endLoc = endLoc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public static MovingFavor createMovingFavorFromDB() {
        return new MovingFavor();
    }
}
