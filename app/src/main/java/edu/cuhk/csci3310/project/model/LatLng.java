package edu.cuhk.csci3310.project.model;


import com.google.firebase.firestore.Exclude;

public class LatLng{
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
