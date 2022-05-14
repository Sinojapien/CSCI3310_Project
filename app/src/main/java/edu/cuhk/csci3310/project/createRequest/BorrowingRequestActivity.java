package edu.cuhk.csci3310.project.createRequest;

import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;

import edu.cuhk.csci3310.project.R;
import edu.cuhk.csci3310.project.SelectionRequestFragment;
import edu.cuhk.csci3310.project.database.Database;
import edu.cuhk.csci3310.project.database.Status;
import edu.cuhk.csci3310.project.database.TaskType;
import edu.cuhk.csci3310.project.model.BorrowingFavor;
import edu.cuhk.csci3310.project.model.GatheringFavor;

public class BorrowingRequestActivity extends RequestActivity {

    TextView borrowEdit;
    LocationRequestFragment locationFragment;
    DescriptionRequestFragment descriptionFragment;
    DateRequestFragment dateFragment;
    TimeRequestFragment timeFragment;
    SelectionRequestFragment selectionFragment;
    Spinner borrowTypeSpinner;
    FirebaseAuth firebaseAuth;

    public static final String TAG = "BorrowRequestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_borrowing);

        setTitle("Borrowing Request");

        borrowEdit = findViewById(R.id.item_edit);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        // Item Type and Purpose
        borrowTypeSpinner = findViewById(R.id.type_spinner);
        setDropDownList(borrowTypeSpinner, getResources().getStringArray(R.array.request_borrowing_type));
        descriptionFragment = DescriptionRequestFragment.newInstance("Purpose:");
        fragmentTransaction.replace(R.id.description_container, descriptionFragment);

        // Borrow Duration
        dateFragment = DateRequestFragment.newInstance(true);
        fragmentTransaction.replace(R.id.date_container, dateFragment);

        // Meetup Time and Location
        locationFragment = LocationRequestFragment.newInstance("Meetup Location:", getMapBoundary(new LatLng(22.418014,	114.207259), 0.075));
        fragmentTransaction.replace(R.id.location_container, locationFragment);
        timeFragment = TimeRequestFragment.newInstance(false);
        fragmentTransaction.replace(R.id.time_container, timeFragment);

        fragmentTransaction.commit();

        firebaseAuth = FirebaseAuth.getInstance();

        findViewById(R.id.post_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAllInformationFilled()){
                    Toast.makeText(view.getContext(), "Please fill in all required info.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent replyIntent = new Intent();
                replyIntent.putExtra(getString(R.string.key_request_type), getResources().getInteger(R.integer.request_type_borrowing));

                // replyIntent.putExtra(getString(R.string.key_request_activity_type), (String) borrowTypeSpinner.getSelectedItem());
                replyIntent.putExtra(getString(R.string.key_request_activity_type), borrowEdit.getText().toString());
                replyIntent.putExtra(getString(R.string.key_request_description), descriptionFragment.getInformationString());

                replyIntent.putExtra(getString(R.string.key_request_location), locationFragment.getInformationLocation());
                replyIntent.putExtra(getString(R.string.key_request_location_string), locationFragment.getInformationString());
                replyIntent.putExtra(getString(R.string.key_request_time), timeFragment.getInformationTime());

                replyIntent.putExtra(getString(R.string.key_request_date), dateFragment.getInformationDate());

                setResult(Activity.RESULT_OK, replyIntent);

                // [Start: Create Dining Request]
                try {
                    BorrowingFavor favor = new BorrowingFavor();
                    favor.setEnquirer(firebaseAuth.getCurrentUser().getUid());
                    favor.setEnquirerName(firebaseAuth.getCurrentUser().getDisplayName());
                    favor.setTaskType(TaskType.BORROWING);
                    favor.setStatus(Status.OPEN);
                    favor.setLocation(new edu.cuhk.csci3310.project.model.LatLng(locationFragment.getInformationLocation()));
                    favor.setDescription(descriptionFragment.getInformationString());
                    favor.setDate(dateFragment.getInformationDate());
                    favor.setTime(timeFragment.getInformationTime());
                    favor.setActivityType(borrowEdit.getText().toString());
                    favor.setItemType((String) borrowTypeSpinner.getSelectedItem());
                    Database.createNewFavor(favor);
                } catch(Exception e) {
                    Log.d(TAG, "onClick: " + e.getMessage());
                }
                // [Stop: Create Dining Request]

                finish();
            }
        });

    }

    @Override
    protected boolean isAllInformationFilled(){
        boolean isAllFilled = true;

        isAllFilled &= borrowEdit.getText().toString().length() > 0;
        isAllFilled &= locationFragment.isFilled();
        isAllFilled &= descriptionFragment.isFilled();
        isAllFilled &= dateFragment.isFilled();
        isAllFilled &= timeFragment.isFilled();

        return isAllFilled;
    }
}