package edu.cuhk.csci3310.project.createRequest;

import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;

import edu.cuhk.csci3310.project.R;
import edu.cuhk.csci3310.project.database.Database;
import edu.cuhk.csci3310.project.database.Status;
import edu.cuhk.csci3310.project.database.TaskType;
import edu.cuhk.csci3310.project.model.DiningFavor;
import edu.cuhk.csci3310.project.model.GatheringFavor;
import edu.cuhk.csci3310.project.myRequests.MyRequestsActivity;

public class GatheringRequestActivity extends RequestActivity {

    LocationRequestFragment locationFragment;
    DescriptionRequestFragment descriptionFragment;
    DateRequestFragment dateFragment;
    TimeRequestFragment timeFragment;
    Spinner gatheringTypeSpinner;
    Spinner participantSpinner;
    FirebaseAuth firebaseAuth;

    public static final String TAG = "GatherRequestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_gathering);

        setTitle("Gathering Request");

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        locationFragment = LocationRequestFragment.newInstance("Gather Location", getMapBoundary(new LatLng(22.418014,	114.207259), 0.075));
        fragmentTransaction.replace(R.id.location_container, locationFragment);

        descriptionFragment = DescriptionRequestFragment.newInstance("Description (if any):");
        fragmentTransaction.replace(R.id.description_container, descriptionFragment);

        dateFragment = DateRequestFragment.newInstance(false);
        fragmentTransaction.replace(R.id.date_container, dateFragment);

        timeFragment = TimeRequestFragment.newInstance(true);
        fragmentTransaction.replace(R.id.time_container, timeFragment);

        fragmentTransaction.commit();

        gatheringTypeSpinner = findViewById(R.id.type_spinner);
        setDropDownList(gatheringTypeSpinner, getResources().getStringArray(R.array.request_gathering_type));

        participantSpinner = findViewById(R.id.participant_spinner);
        setDropDownList(participantSpinner, getNumberStringArray(1, 49));

        firebaseAuth = FirebaseAuth.getInstance();

        findViewById(R.id.post_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAllInformationFilled()){
                    Toast.makeText(view.getContext(), "Please fill in all required info.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent replyIntent = new Intent();
                replyIntent.putExtra(getString(R.string.key_request_type), getResources().getInteger(R.integer.request_type_gathering));

                replyIntent.putExtra(getString(R.string.key_request_location), locationFragment.getInformationLocation());
                replyIntent.putExtra(getString(R.string.key_request_location_string), locationFragment.getInformationString());

                replyIntent.putExtra(getString(R.string.key_request_description), descriptionFragment.getInformationString());

                replyIntent.putExtra(getString(R.string.key_request_date), dateFragment.getInformationDate());
                replyIntent.putExtra(getString(R.string.key_request_time_start), timeFragment.getInformationTime());
                replyIntent.putExtra(getString(R.string.key_request_time_end), timeFragment.getInformationTimeEnd());

                replyIntent.putExtra(getString(R.string.key_request_activity_type), (String) gatheringTypeSpinner.getSelectedItem());
                replyIntent.putExtra(getString(R.string.key_request_participant), Integer.parseInt((String) participantSpinner.getSelectedItem()));

                setResult(Activity.RESULT_OK, replyIntent);

                // [Start: Create Dining Request]
                try {
                    GatheringFavor favor = new GatheringFavor();
                    favor.setEnquirer(firebaseAuth.getCurrentUser().getUid());
                    favor.setTaskType(TaskType.GATHERING);
                    favor.setStatus(Status.OPEN);
                    favor.setLocation(locationFragment.getInformationLocation());
                    favor.setDescription(descriptionFragment.getInformationString());
                    favor.setDate(dateFragment.getInformationDate());
                    favor.setStartTime(timeFragment.getInformationTime());
                    favor.setEndTime(timeFragment.getInformationTimeEnd());
                    favor.setParticipant(Integer.parseInt((String) participantSpinner.getSelectedItem()));
                    favor.setActivityType((String) gatheringTypeSpinner.getSelectedItem());
                    Database.createNewFavor(favor);
                } catch(Exception e) {
                    Log.d(TAG, "onClick: " + e.getMessage());
                }
                // [Stop: Create Dining Request]

                Intent intent = new Intent(GatheringRequestActivity.this, MyRequestsActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected boolean isAllInformationFilled(){
        boolean isAllFilled = true;

        isAllFilled &= locationFragment.isFilled();
        isAllFilled &= descriptionFragment.isFilled();
        isAllFilled &= dateFragment.isFilled();
        isAllFilled &= timeFragment.isFilled();

        return isAllFilled;
    }

}