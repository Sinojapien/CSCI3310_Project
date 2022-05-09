package edu.cuhk.csci3310.project.createRequest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import edu.cuhk.csci3310.project.R;

public class LocationRequestFragment extends Fragment {

    public LatLng mLocation;
    public TextView mTitleText;
    public TextView mTextEdit;
    public Button mMapButton;

    private String mParamTitle;
    private LatLngBounds mParamBoundary;

    private static final String ARG_PARAM_TITLE = "param1";
    private static final String ARG_PARAM_BOUNDARY = "param2";

    private final ActivityResultLauncher<Intent> mapActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // https://developer.android.com/training/basics/intents/result
                        Intent data = result.getData();
                        if (data != null) {
                            Bundle dataExtra = data.getExtras();
                            mTextEdit.setText((String) dataExtra.get(getString(R.string.key_map_title)));;
                            mLocation = (LatLng) dataExtra.get(getString(R.string.key_map_location));
                        }
                    }
                }
            });

    public LocationRequestFragment() {
        // Required empty public constructor
    }

    public static LocationRequestFragment newInstance(String title, LatLngBounds boundary) {
        LocationRequestFragment fragment = new LocationRequestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_TITLE, title);
        args.putParcelable(ARG_PARAM_BOUNDARY, boundary);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamTitle = getArguments().getString(ARG_PARAM_TITLE);
            mParamBoundary = getArguments().getParcelable(ARG_PARAM_BOUNDARY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request_location, container, false);
        mTitleText = view.findViewById(R.id.location_text);
        mTextEdit = view.findViewById(R.id.location_edit);
        mMapButton = view.findViewById(R.id.location_map_button);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTitleText.setText(mParamTitle);
        mMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), RequestMapActivity.class);
                intent.putExtra(getString(R.string.key_map_title), mParamTitle);
                intent.putExtra(getString(R.string.key_map_boundary), mParamBoundary);
                intent.putExtra(getString(R.string.key_map_icon), BitmapDescriptorFactory.HUE_RED);

                mapActivityLauncher.launch(intent);
            }
        });

    }

    public boolean isFilled(){
        return mLocation != null && mTextEdit.getText().toString().length() > 0;
    }

    public String getInformationString(){
        return mTextEdit.getText().toString();
    }

    public LatLng getInformationLocation(){
        return mLocation;
    }

}