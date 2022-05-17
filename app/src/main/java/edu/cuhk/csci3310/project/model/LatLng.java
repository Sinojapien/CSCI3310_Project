package edu.cuhk.csci3310.project.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.Exclude;

// since firebase complaint when serializing google's LatLng, (no empty constructor)
// use our own class to wrap it up
public class LatLng implements Parcelable {
    private Double latitude;
    private Double longitude;

    public LatLng() {}

    public LatLng(Double latitude, Double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public LatLng(com.google.android.gms.maps.model.LatLng googleLatLng){
        this.longitude = googleLatLng.longitude;
        this.latitude = googleLatLng.latitude;
    }

    protected LatLng(Parcel in) {
        if (in.readByte() == 0) {
            latitude = null;
        } else {
            latitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            longitude = null;
        } else {
            longitude = in.readDouble();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (latitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(latitude);
        }
        if (longitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(longitude);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LatLng> CREATOR = new Creator<LatLng>() {
        @Override
        public LatLng createFromParcel(Parcel in) {
            return new LatLng(in);
        }

        @Override
        public LatLng[] newArray(int size) {
            return new LatLng[size];
        }
    };

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    // conversion method, required for using google map services
    @Exclude
    public com.google.android.gms.maps.model.LatLng getGoogleLatLng(){
        return new com.google.android.gms.maps.model.LatLng(this.getLatitude(),
                this.getLongitude());
    }
}
