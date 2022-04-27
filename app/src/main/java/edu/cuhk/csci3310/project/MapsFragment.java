package edu.cuhk.csci3310.project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.LinkedList;

public class MapsFragment extends Fragment{

    private float defaultIconColour;
    private LatLng defaultLocation;
    private String defaultTitle;
    private LatLngBounds defaultMapBoundary;

    final float defaultZoom = 16.0f;
    final float selectZoom = 18.0f;
    private GoogleMap mMap;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            mMap.getUiSettings().setZoomGesturesEnabled(false);
            mMap.getUiSettings().setZoomControlsEnabled(false);

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

    public static MapsFragment newInstance(){
        MapsFragment mapFragment = new MapsFragment();
        mapFragment.defaultLocation = new LatLng(22.418014,	114.207259);
        mapFragment.defaultMapBoundary = new LatLngBounds(new LatLng(22.418014-0.005,	114.207259-0.005), new LatLng(22.418014+0.005,	114.207259+0.005));
        mapFragment.defaultIconColour = BitmapDescriptorFactory.HUE_RED;
        mapFragment.defaultTitle = "Location";
        return mapFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
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