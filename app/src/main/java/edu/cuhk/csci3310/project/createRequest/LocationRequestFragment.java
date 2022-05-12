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
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import edu.cuhk.csci3310.project.R;
import edu.cuhk.csci3310.project.SelectionRequestFragment;

public class LocationRequestFragment extends RequestFragment {

    // Views
    public TextView mTitleText;
    public EditText mTextEdit;
    public Button mMapButton;

    // Variables
    public LatLng mLocation;
    //private LatLngBounds mParamBoundary;
    private String mParamTitle;
    private int mLocationInfoID;
    private boolean mWebLocation;

    private static final String ARG_PARAM_TITLE = "param1";
    //private static final String ARG_PARAM_BOUNDARY = "param2";
    private static final String ARG_PARAM_INFO = "param3";
    private static final String INSTANCE_LOCATION = "param5";
    private static final String INSTANCE_WEB = "param6";


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

    public static LocationRequestFragment newInstance(String title, int rid) { // , LatLngBounds boundary
        LocationRequestFragment fragment = new LocationRequestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_TITLE, title);
        args.putInt(ARG_PARAM_INFO, rid);
        //args.putParcelable(ARG_PARAM_BOUNDARY, boundary);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamTitle = getArguments().getString(ARG_PARAM_TITLE);
            mLocationInfoID = getArguments().getInt(ARG_PARAM_INFO);
            //mParamBoundary = getArguments().getParcelable(ARG_PARAM_BOUNDARY);
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
                if (mWebLocation) return; // implement web activity

                Intent intent = new Intent(view.getContext(), RequestMapActivity.class);
                intent.putExtra(getString(R.string.key_map_location), mLocation);
                intent.putExtra(getString(R.string.key_map_title), mParamTitle);
                intent.putExtra(getString(R.string.key_map_info), mLocationInfoID);
                //intent.putExtra(getString(R.string.key_map_boundary), mParamBoundary);
                intent.putExtra(getString(R.string.key_map_icon), BitmapDescriptorFactory.HUE_RED);

                mapActivityLauncher.launch(intent);
            }
        });

        mMapButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mWebLocation){
                    mMapButton.setText("M");
                }else{
                    mMapButton.setText("Web");
                }

                mWebLocation = !mWebLocation;

                return false;
            }
        });

    }

    @Override
    public void onSaveInstanceState(@Nullable Bundle savedInstanceState){
        savedInstanceState.putString(ARG_PARAM_TITLE, mTitleText.getText().toString());
        //savedInstanceState.putParcelable(ARG_PARAM_BOUNDARY, mParamBoundary);
        savedInstanceState.putInt(ARG_PARAM_INFO, mLocationInfoID);
        savedInstanceState.putParcelable(INSTANCE_LOCATION, mLocation);
        savedInstanceState.putBoolean(INSTANCE_WEB, mWebLocation);
    }

    @Override
    protected void onLoadInstanceState(@Nullable Bundle savedInstanceState){
        mParamTitle = savedInstanceState.getString(ARG_PARAM_TITLE);
        //mParamBoundary = savedInstanceState.getParcelable(ARG_PARAM_BOUNDARY);
        mLocationInfoID = savedInstanceState.getInt(ARG_PARAM_INFO);
        mLocation = savedInstanceState.getParcelable(INSTANCE_LOCATION);
        mWebLocation = savedInstanceState.getBoolean(INSTANCE_WEB);
    }


    @Override
    public boolean isFilled(){
        if (mWebLocation)
            return mTextEdit.getText().toString().length() > 0;
        return mLocation != null && mTextEdit.getText().toString().length() > 0;
    }

    public String getInformationString(){
        return mTextEdit.getText().toString();
    }

    public LatLng getInformationLocation(){
        if (mWebLocation)
            return null;
        return mLocation;
    }

}