package edu.cuhk.csci3310.project;

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

    final float defaultZoom = 16.0f;
    final float selectZoom = 18.0f;
    private GoogleMap mMap;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            mMap.getUiSettings().setCompassEnabled(false);
            mMap.getUiSettings().setZoomGesturesEnabled(false);
            mMap.getUiSettings().setZoomControlsEnabled(true);

            // https://developers.google.com/maps/documentation/android-sdk/views#restricting_the_users_panning_to_a_given_area
            mMap.setLatLngBoundsForCameraTarget(defaultMapBoundary);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultMapBoundary.getCenter(), defaultZoom));


            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng){
                    mMap.clear();
                    focusOnMarker(addMarker(latLng));
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
        defaultLocation = (LatLng) intentData.get(getString(R.string.key_map_location));
        defaultMapBoundary = (LatLngBounds) intentData.get(getString(R.string.key_map_boundary));
        defaultIconColour = (float) intentData.get(getString(R.string.key_map_icon));
        defaultTitle = (String) intentData.get(getString(R.string.key_map_title));
//        defaultLocation = new LatLng(22.418014,	114.207259);
//        defaultMapBoundary = new LatLngBounds(new LatLng(22.418014-0.005,	114.207259-0.005), new LatLng(22.418014+0.005,	114.207259+0.005));
//        defaultIconColour = BitmapDescriptorFactory.HUE_RED;
//        defaultTitle = "Location";

        // MapsFragment mapFragment = MapsFragment.newInstance();
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        mapFragment.getMapAsync(callback);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.map_container, mapFragment).commit();

        findViewById(R.id.confirm_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                getSupportFragmentManager().findFragmentByTag("");
                replyIntent.putExtra("TEST", "OK");
                setResult(Activity.RESULT_OK, replyIntent);
                finish();
            }
        });
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
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), selectZoom));
            // mMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
            // mMap.animateCamera(CameraUpdateFactory.zoomTo(buildingZoomModeZoom));
            // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), buildingZoomModeZoom));
        }
    }

}