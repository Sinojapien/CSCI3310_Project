package edu.cuhk.csci3310.project.requestDetails.favorFragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import edu.cuhk.csci3310.project.R;
import edu.cuhk.csci3310.project.model.TutoringFavor;

public class TutoringFragment extends Fragment {

    public static final String BUNDLE_KEY = "Favor";
    TutoringFavor favor;

    // View attributes
    private TextView descriptionTV;
    private TextView dateStartTV;
    private TextView dateEndTV;
    private TextView timeStartTV;
    private TextView timeEndTV;
    private TextView tutoringTypeTV;
    private TextView courseCodeTV;
    private TextView participantTV;

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
            if(favor.getLocation() != null) {
                LatLng location = new LatLng(favor.getLocation().getLatitude(), favor.getLocation().getLongitude());
                googleMap.addMarker(new MarkerOptions().position(location).title("Location"));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 16.0f));
            }
        }
    };

    public static Fragment newInstance(TutoringFavor favor) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_KEY, favor);
        Fragment fragment = new TutoringFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tutoring, container, false);
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
        descriptionTV = view.findViewById(R.id.description_TV);
        dateStartTV = view.findViewById(R.id.date_start_TV);
        dateEndTV = view.findViewById(R.id.date_end_TV);
        timeStartTV = view.findViewById(R.id.time_start_TV);
        timeEndTV = view.findViewById(R.id.time_end_TV);
        tutoringTypeTV = view.findViewById(R.id.tutoring_type_TV);
        courseCodeTV = view.findViewById(R.id.course_code_TV);
        participantTV = view.findViewById(R.id.participant_TV);
        // Get favor attribute
        if(getArguments().getParcelable(BUNDLE_KEY) != null) {
            favor = getArguments().getParcelable(BUNDLE_KEY);
            if(favor.getDescription() != null) {
                descriptionTV.setText(favor.getDescription());
            }
            if(favor.getStartDate() != null) {
                dateStartTV.setText(favor.getStartDate());
            }
            if(favor.getEndDate() != null) {
                dateEndTV.setText(favor.getEndDate());
            }
            if(favor.getStartTime() != null) {
                timeStartTV.setText(favor.getStartTime());
            }
            if(favor.getEndTime() != null) {
                timeEndTV.setText(favor.getEndTime());
            }
            if(favor.getSelection() != null) {
                List<String> selection = favor.getSelection();
                String selectionString = "";
                for(String type : selection) {
                    selectionString += type + ", ";
                }
                if (selectionString != null && selectionString.length() > 0 && selectionString.charAt(selectionString.length() - 2) == ',') {
                    selectionString = selectionString.substring(0, selectionString.length() - 2);
                }
                tutoringTypeTV.setText(selectionString);
            }
            if(favor.getCourseCode() != null) {
                courseCodeTV.setText(favor.getCourseCode());
            }
            participantTV.setText("" + favor.getCourseParticipant());
        }
    }
}