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

    //TextView borrowEdit;
    SelectionRequestFragment selectionFragment;
    LocationRequestFragment locationFragment;
    DescriptionRequestFragment descriptionFragment;
    DateRequestFragment dateFragment;
    TimeRequestFragment timeFragment;
    Spinner borrowTypeSpinner;
    FirebaseAuth firebaseAuth;

    public static final String TAG = "BorrowRequestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_borrowing);

        setTitle("Borrowing Request");

        //borrowEdit = findViewById(R.id.item_edit);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        // Item Type and Purpose
        borrowTypeSpinner = findViewById(R.id.type_spinner);
        setDropDownList(borrowTypeSpinner, getResources().getStringArray(R.array.request_borrowing_type));

        if (selectionFragment == null)
            selectionFragment = SelectionRequestFragment.newInstance(null, "Enter item names:", null);
        fragmentTransaction.replace(R.id.selection_container, selectionFragment);

        if (descriptionFragment == null)
            descriptionFragment = DescriptionRequestFragment.newInstance("Purpose:");
        fragmentTransaction.replace(R.id.description_container, descriptionFragment);

        // Borrow Duration
        if (dateFragment == null)
            dateFragment = DateRequestFragment.newInstance("Borrow Duration:", true);
        fragmentTransaction.replace(R.id.date_container, dateFragment);

        // Meetup Time and Location
        if (locationFragment == null)
            locationFragment = LocationRequestFragment.newInstance("Meetup Location:", R.array.location_cuhk);
            //locationFragment = LocationRequestFragment.newInstance("Meetup Location:", getMapBoundary(new LatLng(22.418014,	114.207259), 0.075));
        fragmentTransaction.replace(R.id.location_container, locationFragment);
        if (timeFragment == null)
            timeFragment = TimeRequestFragment.newInstance("Meetup Time:", false);
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

                //replyIntent.putExtra(getString(R.string.key_request_activity_type), (String) borrowTypeSpinner.getSelectedItem());
                //replyIntent.putExtra(getString(R.string.key_request_activity_type), borrowEdit.getText().toString());
                replyIntent.putExtra(getString(R.string.key_request_selection), selectionFragment.getInformationStringList());
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
                    favor.setTaskType(TaskType.BORROWING);
                    favor.setStatus(Status.OPEN);
                    favor.setLocation(locationFragment.getInformationLocation());
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

        onLoadInstanceStateLate(savedInstanceState);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.putFragment(outState, getResources().getResourceName(R.id.selection_container), selectionFragment);
        fragmentManager.putFragment(outState, getResources().getResourceName(R.id.location_container), locationFragment);
        fragmentManager.putFragment(outState, getResources().getResourceName(R.id.description_container), descriptionFragment);
        fragmentManager.putFragment(outState, getResources().getResourceName(R.id.date_container), dateFragment);
        fragmentManager.putFragment(outState, getResources().getResourceName(R.id.time_container), timeFragment);
        outState.putInt(getResources().getResourceName(R.id.type_spinner), borrowTypeSpinner.getSelectedItemPosition());
    }

    @Override
    protected void onLoadInstanceState(@Nullable Bundle savedInstanceState){
        FragmentManager fragmentManager = getSupportFragmentManager();
        selectionFragment = (SelectionRequestFragment) fragmentManager.getFragment(savedInstanceState, getResources().getResourceName(R.id.selection_container));
        locationFragment = (LocationRequestFragment) fragmentManager.getFragment(savedInstanceState, getResources().getResourceName(R.id.location_container));
        descriptionFragment = (DescriptionRequestFragment) fragmentManager.getFragment(savedInstanceState, getResources().getResourceName(R.id.description_container));
        dateFragment = (DateRequestFragment) fragmentManager.getFragment(savedInstanceState, getResources().getResourceName(R.id.date_container));
        timeFragment = (TimeRequestFragment) fragmentManager.getFragment(savedInstanceState, getResources().getResourceName(R.id.time_container));
    }

    protected void onLoadInstanceStateLate(@Nullable Bundle savedInstanceState){
        if (savedInstanceState != null){
            borrowTypeSpinner.setSelection(savedInstanceState.getInt(getResources().getResourceName(R.id.type_spinner)));
        }
    }

    @Override
    protected boolean isAllInformationFilled(){
        boolean isAllFilled = true;

        //isAllFilled &= borrowEdit.getText().toString().length() > 0;
        isAllFilled &= selectionFragment.isFilled();
        isAllFilled &= locationFragment.isFilled();
        isAllFilled &= descriptionFragment.isFilled();
        isAllFilled &= dateFragment.isFilled();
        isAllFilled &= timeFragment.isFilled();

        return isAllFilled;
    }
}