package edu.cuhk.csci3310.project.requestDetails.favorFragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.cuhk.csci3310.project.R;
import edu.cuhk.csci3310.project.model.MovingFavor;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MovingFragment extends Fragment {

    public static final String BUNDLE_KEY = "Favor";
    MovingFavor favor;

    // View attributes
    private TextView dateTV;
    private TextView timeTV;

    private final String TAG = "MovingFragment";


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
            // Set marker at start loc
            if(favor.getStartLoc() != null) {
                LatLng startloc = new LatLng(favor.getStartLoc().getLatitude(), favor.getEndLoc().getLongitude());
                googleMap.addMarker(new MarkerOptions().position(startloc).title("Start Locarion"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(startloc));
            }
            // Set marker at end loc
            if(favor.getEndLoc() != null) {
                LatLng endLoc = new LatLng(favor.getStartLoc().getLatitude(), favor.getEndLoc().getLongitude());
                googleMap.addMarker(new MarkerOptions().position(endLoc).title("End Location"));
            }
        }
    };

    public static Fragment newInstance(MovingFavor favor) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_KEY, favor);
        Fragment fragment = new MovingFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_moving, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
        // Instantiate view attributes
        dateTV = view.findViewById(R.id.date_TV);
        timeTV = view.findViewById(R.id.time_TV);
        if(getArguments().getParcelable(BUNDLE_KEY) != null) {
            favor = getArguments().getParcelable(BUNDLE_KEY);
            // Fill views
            if(favor.getDate() != null) {
                dateTV.setText(favor.getDate());
            }
            if(favor.getTime() != null) {
                timeTV.setText(favor.getTime());
            }
        }
    }
}