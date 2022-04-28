package edu.cuhk.csci3310.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class RequestMapActivity extends AppCompatActivity {

    private float defaultIconColour;
    private LatLng defaultLocation;
    private String defaultTitle;
    private LatLngBounds defaultMapBoundary;

    private LatLng selectedLocation;

    final float defaultZoom = 16.0f;
    final float selectedZoom = 18.0f;
    final float minZoom = 16.0f;
    final float maxZoom = 20.0f;

    private GoogleMap mMap;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            mMap.getUiSettings().setCompassEnabled(false);
            mMap.getUiSettings().setZoomControlsEnabled(false);
            mMap.getUiSettings().setMapToolbarEnabled(false);
            mMap.getUiSettings().setZoomGesturesEnabled(true);
            mMap.setMinZoomPreference(minZoom);
            mMap.setMaxZoomPreference(maxZoom);

            // https://developers.google.com/maps/documentation/android-sdk/views#restricting_the_users_panning_to_a_given_area
            // https://developers.google.com/maps/documentation/android-sdk/views#restrict-panning
            mMap.setLatLngBoundsForCameraTarget(defaultMapBoundary);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultMapBoundary.getCenter(), defaultZoom));

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(@NonNull LatLng latLng){
                    mMap.clear();
                    selectedLocation = latLng;
                    // focusOnMarker(addMarker(latLng));
                    addMarker(latLng).showInfoWindow();
                }
            });

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    marker.remove();
                    focusOnMarker(null);
                    return false;
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_map);

        Bundle intentData = getIntent().getExtras();
        // defaultLocation = (LatLng) intentData.get(getString(R.string.key_map_location));
        defaultMapBoundary = (LatLngBounds) intentData.get(getString(R.string.key_map_boundary));
        defaultIconColour = (float) intentData.get(getString(R.string.key_map_icon));
        defaultTitle = (String) intentData.get(getString(R.string.key_map_title));

        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        mapFragment.getMapAsync(callback);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.map_container, mapFragment).commit();

        findViewById(R.id.confirm_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                replyIntent.putExtra(getString(R.string.key_map_location), selectedLocation);
                setResult(Activity.RESULT_OK, replyIntent);
                finish();
            }
        });
    }

    private String getGeoCode(LatLng latLng){
        // https://www.youtube.com/watch?v=Nsl99WWDFxM
        // https://developers.google.com/maps/documentation/geocoding/overview
        // https://www.geeksforgeeks.org/google-cloud-platform-creating-google-cloud-console-account-projects/
        return "";
    }

    private Marker addMarker(LatLng latLng){
        // two markers?
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(defaultTitle);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(defaultIconColour));
        return mMap.addMarker(markerOptions);
    }

    private void focusOnMarker(Marker marker) {
        if (marker == null){
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(centerPosition));
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(defaultZoom));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(defaultZoom));
            // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(centerPosition, campusModeZoom));
        }else {
//            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            marker.showInfoWindow();

            // Can't move camera and zoom with animateCamera()
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), selectedZoom));
            // mMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
            // mMap.animateCamera(CameraUpdateFactory.zoomTo(buildingZoomModeZoom));
            // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), buildingZoomModeZoom));
        }
    }

}