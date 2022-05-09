package edu.cuhk.csci3310.project.createRequest;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;

import edu.cuhk.csci3310.project.R;
import edu.cuhk.csci3310.project.SelectionRequestFragment;

public class TutoringRequestActivity extends RequestActivity {

    LocationRequestFragment locationFragment;
    DescriptionRequestFragment descriptionFragment;
    DateRequestFragment dateFragment;
    TimeRequestFragment timeFragment;
    SelectionRequestFragment selectionFragment;
    PictureRequestFragment pictureFragment;
    TextView courseCodeEdit;
    Spinner participantSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_tutoring);

        setTitle("Tutoring Request");

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        locationFragment = LocationRequestFragment.newInstance("Start Location", getMapBoundary(new LatLng(22.418014,	114.207259), 0.075));
        fragmentTransaction.replace(R.id.location_container, locationFragment);

        descriptionFragment = DescriptionRequestFragment.newInstance("Description (if any):");
        fragmentTransaction.replace(R.id.description_container, descriptionFragment);

        dateFragment = DateRequestFragment.newInstance(null, true);
        fragmentTransaction.replace(R.id.date_container, dateFragment);

        // Not necessary???
        timeFragment = TimeRequestFragment.newInstance(null, true);
        fragmentTransaction.replace(R.id.time_container, timeFragment);

        ArrayList<String> itemList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.request_tutoring_type)));
        selectionFragment = SelectionRequestFragment.newInstance(null, "Choose Tutoring Type:", itemList);
        fragmentTransaction.replace(R.id.selection_container, selectionFragment);

        pictureFragment = PictureRequestFragment.newInstance(findViewById(R.id.expanded_image));
        fragmentTransaction.replace(R.id.picture_container, pictureFragment);

        fragmentTransaction.commit();

        ConstraintLayout tutorInformation = findViewById(R.id.tutor_info_view);
        courseCodeEdit = tutorInformation.findViewById(R.id.course_code_edit);
        setCourseCodeTextEdit(courseCodeEdit);
        participantSpinner = tutorInformation.findViewById(R.id.participant_spinner);
        setDropDownList(participantSpinner, getNumberStringArray(1, 9));

        findViewById(R.id.post_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAllInformationFilled()){
                    Toast.makeText(view.getContext(), "Please fill in all required info.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent replyIntent = new Intent();
                replyIntent.putExtra(getString(R.string.key_request_type), getResources().getInteger(R.integer.request_type_tutoring));
                replyIntent.putExtra(getString(R.string.key_request_location), locationFragment.getInformationLocation());
                replyIntent.putExtra(getString(R.string.key_request_location_string), locationFragment.getInformationString());
                replyIntent.putExtra(getString(R.string.key_request_description), descriptionFragment.getInformationString());
                replyIntent.putExtra(getString(R.string.key_request_date_start), dateFragment.getInformationDate());
                replyIntent.putExtra(getString(R.string.key_request_date_end), dateFragment.getInformationDateEnd());
                replyIntent.putExtra(getString(R.string.key_request_time_start), timeFragment.getInformationTime());
                replyIntent.putExtra(getString(R.string.key_request_time_end), timeFragment.getInformationTimeEnd());

                replyIntent.putExtra(getString(R.string.key_request_selection), selectionFragment.getInformationStringList());
                replyIntent.putExtra(getString(R.string.key_request_picture), pictureFragment.getInformationBitmap());

                replyIntent.putExtra(getString(R.string.key_request_course_code), courseCodeEdit.getText().toString());
                replyIntent.putExtra(getString(R.string.key_request_participant), (int) participantSpinner.getSelectedItem());

                setResult(Activity.RESULT_OK, replyIntent);
                finish();
            }
        });

    }

    @Override
    protected boolean isAllInformationFilled(){
        boolean isAllFilled = true;

        isAllFilled &= locationFragment.isFilled();
        isAllFilled &= dateFragment.isFilled();
        isAllFilled &= timeFragment.isFilled();
        isAllFilled &= selectionFragment.isFilled();
        isAllFilled &= courseCodeEdit.getText().toString().length() > 0;

        return isAllFilled;
    }

}