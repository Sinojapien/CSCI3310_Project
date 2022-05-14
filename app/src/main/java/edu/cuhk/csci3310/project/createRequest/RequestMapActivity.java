package edu.cuhk.csci3310.project.createRequest;

// Name: Yeung Chi Ho, SID: 1155126460

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import edu.cuhk.csci3310.project.R;

public class RequestMapActivity extends AppCompatActivity {

    private float defaultIconColour;
    private LatLng defaultLocation;
    private String defaultTitle;
    private LatLngBounds defaultMapBoundary;

    private LatLng selectedLocation;

    float defaultZoom;
    float defaultSelectedZoom;
    float defaultMinZoom;
    float defaultMaxZoom;

    private GoogleMap mMap;
    SupportMapFragment mapFragment;

    // https://developers.google.com/maps/documentation/places/web-service/search#PlaceSearchRequests
    // https://stackoverflow.com/questions/30161395/im-trying-to-search-nearby-places-such-as-banks-restaurants-atms-inside-the-d

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            mMap.getUiSettings().setCompassEnabled(false);
            //mMap.getUiSettings().setZoomControlsEnabled(false);
            //mMap.getUiSettings().setMapToolbarEnabled(false);
            //mMap.getUiSettings().setRotateGesturesEnabled(false);
            mMap.getUiSettings().setZoomGesturesEnabled(true);
            mMap.setMinZoomPreference(defaultMinZoom);
            mMap.setMaxZoomPreference(defaultMaxZoom);

            // https://developers.google.com/maps/documentation/android-sdk/views#restricting_the_users_panning_to_a_given_area
            // https://developers.google.com/maps/documentation/android-sdk/views#restrict-panning
            mMap.setLatLngBoundsForCameraTarget(defaultMapBoundary);

            if (defaultLocation != null){
                focusOnMarker(addMarker(defaultLocation));
                selectedLocation = defaultLocation;
            }else{
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultMapBoundary.getCenter(), defaultZoom));
            }

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(@NonNull LatLng latLng){
                    mMap.clear();
                    selectedLocation = latLng;
                    addMarker(latLng).showInfoWindow();
                }
            });

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {
                    marker.remove();
                    return false;
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_map);

        loadIntentExtra(getIntent().getExtras());

        if (savedInstanceState != null)
            onLoadInstanceState(savedInstanceState);

        if (mapFragment == null)
            mapFragment = SupportMapFragment.newInstance();
        mapFragment.getMapAsync(callback);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.map_container, mapFragment).commit();

        findViewById(R.id.confirm_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFilled()){
                    Toast.makeText(view.getContext(), "Please select " + defaultTitle + ".", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent replyIntent = new Intent();
                replyIntent.putExtra(getString(R.string.key_map_title), selectedLocation.toString());
                replyIntent.putExtra(getString(R.string.key_map_location), selectedLocation);
                setResult(Activity.RESULT_OK, replyIntent);
                finish();
            }
        });
    }

    protected void loadIntentExtra(Bundle intentData){
        defaultLocation = (LatLng) intentData.get(getString(R.string.key_map_location));
        //defaultMapBoundary = (LatLngBounds) intentData.get(getString(R.string.key_map_boundary));
        defaultIconColour = (float) intentData.get(getString(R.string.key_map_icon));
        defaultTitle = (String) intentData.get(getString(R.string.key_map_title));

        String[] locationInformation = getResources().getStringArray(intentData.getInt(getString(R.string.key_map_info)));
        LatLng centerLocation = new LatLng(Float.parseFloat(locationInformation[0]), Float.parseFloat(locationInformation[1]));
        defaultMapBoundary = getMapBoundary(centerLocation, Float.parseFloat(locationInformation[2]));
        defaultMinZoom = Float.parseFloat(locationInformation[3]);
        defaultMaxZoom = Float.parseFloat(locationInformation[4]);

        defaultZoom = defaultMinZoom;
        defaultSelectedZoom = (defaultMinZoom + defaultMaxZoom) / 2;
    }

    @Override
    public void onSaveInstanceState(@Nullable Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        getSupportFragmentManager().putFragment(savedInstanceState, "MAP_FRAGMENT", mapFragment);
        savedInstanceState.putParcelable(getString(R.string.key_map_location), selectedLocation);
        savedInstanceState.putParcelable(getString(R.string.key_map_boundary), defaultMapBoundary);
        savedInstanceState.putFloat(getString(R.string.key_map_icon), defaultIconColour);
        savedInstanceState.putString(getString(R.string.key_map_title), defaultTitle);
    }

    protected void onLoadInstanceState(@Nullable Bundle savedInstanceState){
        mapFragment = (SupportMapFragment) getSupportFragmentManager().getFragment(savedInstanceState, "MAP_FRAGMENT");
        defaultLocation = (LatLng) savedInstanceState.get(getString(R.string.key_map_location));
        defaultMapBoundary = (LatLngBounds) savedInstanceState.get(getString(R.string.key_map_boundary));
        defaultIconColour = (float) savedInstanceState.get(getString(R.string.key_map_icon));
        defaultTitle = (String) savedInstanceState.get(getString(R.string.key_map_title));
    }

    public static LatLngBounds getMapBoundary(LatLng center, double offset){
        LatLng boundaryNE = new LatLng(center.latitude - offset,	center.longitude - offset);
        LatLng boundarySW = new LatLng(center.latitude + offset,	center.longitude + offset);
        return new LatLngBounds(boundaryNE, boundarySW);
    }

    private String getReverseGeoCode(LatLng latLng){
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
            mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultMapBoundary.getCenter()));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(defaultZoom));
            //mMap.animateCamera(CameraUpdateFactory.zoomTo(defaultZoom));
            //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(centerPosition, campusModeZoom));
        }else {
//            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            marker.showInfoWindow();

            // Can't move camera and zoom with animateCamera()
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), defaultSelectedZoom));
            // mMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
            // mMap.animateCamera(CameraUpdateFactory.zoomTo(buildingZoomModeZoom));
            // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), buildingZoomModeZoom));
        }
    }

    private boolean isFilled(){
        return selectedLocation != null;
    }

}