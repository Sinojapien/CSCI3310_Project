package edu.cuhk.csci3310.project.createRequest;

import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import edu.cuhk.csci3310.project.R;
import edu.cuhk.csci3310.project.SelectionRequestFragment;

public class BorrowingRequestActivity extends RequestActivity {

    //TextView borrowEdit;
    SelectionRequestFragment selectionFragment;
    LocationRequestFragment locationFragment;
    DescriptionRequestFragment descriptionFragment;
    DateRequestFragment dateFragment;
    TimeRequestFragment timeFragment;
    Spinner borrowTypeSpinner;

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

        selectionFragment = SelectionRequestFragment.newInstance(null, "Enter item names:", null);
        fragmentTransaction.replace(R.id.selection_container, selectionFragment);

        descriptionFragment = DescriptionRequestFragment.newInstance("Purpose:");
        fragmentTransaction.replace(R.id.description_container, descriptionFragment);

        // Borrow Duration
        dateFragment = DateRequestFragment.newInstance("Borrow Duration:", true);
        fragmentTransaction.replace(R.id.date_container, dateFragment);

        // Meetup Time and Location
        locationFragment = LocationRequestFragment.newInstance("Meetup Location:", getMapBoundary(new LatLng(22.418014,	114.207259), 0.075));
        fragmentTransaction.replace(R.id.location_container, locationFragment);
        timeFragment = TimeRequestFragment.newInstance("Meetup Time:", false);
        fragmentTransaction.replace(R.id.time_container, timeFragment);

        fragmentTransaction.commit();

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
                replyIntent.putExtra(getString(R.string.key_request_date), dateFragment.getInformationDate());

                setResult(Activity.RESULT_OK, replyIntent);
                finish();
            }
        });

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