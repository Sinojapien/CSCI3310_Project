package edu.cuhk.csci3310.project.createRequest;

// Name: Yeung Chi Ho, SID: 1155126460

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import edu.cuhk.csci3310.project.R;

public class DiningRequestActivity extends RequestActivity {

    LocationRequestFragment locationFragment;
    DescriptionRequestFragment descriptionFragment;
    DateRequestFragment dateFragment;
    TimeRequestFragment timeFragment;
    Spinner participantSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_dining);

        setTitle("Dining Request");

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (locationFragment == null)
            locationFragment = LocationRequestFragment.newInstance("Restaurant Location", R.array.location_cuhk);
            //locationFragment = LocationRequestFragment.newInstance("Restaurant Location", getMapBoundary(new LatLng(22.418014, 114.207259), 0.075));
        fragmentTransaction.replace(R.id.location_container, locationFragment);

        if (descriptionFragment == null)
            descriptionFragment = DescriptionRequestFragment.newInstance("Description (if any):");
        fragmentTransaction.replace(R.id.description_container, descriptionFragment);

        if (dateFragment == null)
            dateFragment = DateRequestFragment.newInstance(null, false);
        fragmentTransaction.replace(R.id.date_container, dateFragment);

        if (timeFragment == null)
            timeFragment = TimeRequestFragment.newInstance(null, true);
        fragmentTransaction.replace(R.id.time_container, timeFragment);

        fragmentTransaction.commit();

        participantSpinner = findViewById(R.id.participant_spinner);
        setDropDownList(participantSpinner, getNumberStringArray(1, 9));

        findViewById(R.id.post_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAllInformationFilled()){
                    Toast.makeText(view.getContext(), "Please fill in all required info.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent replyIntent = new Intent();
                replyIntent.putExtra(getString(R.string.key_request_type), getResources().getInteger(R.integer.request_type_dining));

                replyIntent.putExtra(getString(R.string.key_request_location), locationFragment.getInformationLocation());
                replyIntent.putExtra(getString(R.string.key_request_location_string), locationFragment.getInformationString());

                replyIntent.putExtra(getString(R.string.key_request_description), descriptionFragment.getInformationString());

                replyIntent.putExtra(getString(R.string.key_request_date), dateFragment.getInformationDate());
                replyIntent.putExtra(getString(R.string.key_request_time_start), timeFragment.getInformationTime());
                replyIntent.putExtra(getString(R.string.key_request_time_end), timeFragment.getInformationTimeEnd());

                replyIntent.putExtra(getString(R.string.key_request_participant), Integer.parseInt((String) participantSpinner.getSelectedItem()));

                setResult(Activity.RESULT_OK, replyIntent);
                finish();
            }
        });

        onLoadInstanceStateLate(savedInstanceState);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.putFragment(outState, getResources().getResourceName(R.id.location_container), locationFragment);
        fragmentManager.putFragment(outState, getResources().getResourceName(R.id.description_container), descriptionFragment);
        fragmentManager.putFragment(outState, getResources().getResourceName(R.id.date_container), dateFragment);
        fragmentManager.putFragment(outState, getResources().getResourceName(R.id.time_container), timeFragment);
        outState.putInt(getResources().getResourceName(R.id.participant_spinner), participantSpinner.getSelectedItemPosition());
    }

    @Override
    protected void onLoadInstanceState(@Nullable Bundle savedInstanceState){
        FragmentManager fragmentManager = getSupportFragmentManager();
        locationFragment = (LocationRequestFragment) fragmentManager.getFragment(savedInstanceState, getResources().getResourceName(R.id.location_container));
        descriptionFragment = (DescriptionRequestFragment) fragmentManager.getFragment(savedInstanceState, getResources().getResourceName(R.id.description_container));
        dateFragment = (DateRequestFragment) fragmentManager.getFragment(savedInstanceState, getResources().getResourceName(R.id.date_container));
        timeFragment = (TimeRequestFragment) fragmentManager.getFragment(savedInstanceState, getResources().getResourceName(R.id.time_container));
    }

    protected void onLoadInstanceStateLate(@Nullable Bundle savedInstanceState){
        if (savedInstanceState != null){
            participantSpinner.setSelection(savedInstanceState.getInt(getResources().getResourceName(R.id.participant_spinner)));
        }
    }

    @Override
    protected boolean isAllInformationFilled(){
        boolean isAllFilled = true;

        isAllFilled &= locationFragment.isFilled();
        isAllFilled &= dateFragment.isFilled();
        isAllFilled &= timeFragment.isFilled();

        return isAllFilled;
    }

}