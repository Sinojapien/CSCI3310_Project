package edu.cuhk.csci3310.project.createRequest;

// Name: Yeung Chi Ho, SID: 1155126460

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;

import edu.cuhk.csci3310.project.R;
import edu.cuhk.csci3310.project.SelectionRequestFragment;
import edu.cuhk.csci3310.project.database.Database;
import edu.cuhk.csci3310.project.database.Status;
import edu.cuhk.csci3310.project.database.TaskType;
import edu.cuhk.csci3310.project.model.TutoringFavor;

public class TutoringRequestActivity extends RequestActivity {

    LocationRequestFragment locationFragment;
    DescriptionRequestFragment descriptionFragment;
    DateRequestFragment dateFragment;
    TimeRequestFragment timeFragment;
    SelectionRequestFragment selectionFragment;
    //PictureRequestFragment pictureFragment;
    TextView courseCodeEdit;
    Spinner participantSpinner;
    FirebaseAuth firebaseAuth;

    public static final String TAG = "TutoringRequestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_tutoring);

        setTitle("Tutoring Request");

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (locationFragment == null)
            //locationFragment = LocationRequestFragment.newInstance("Location:", getMapBoundary(new LatLng(22.418014,	114.207259), 0.075));
            locationFragment = LocationRequestFragment.newInstance("Location:", R.array.location_cuhk);
        fragmentTransaction.replace(R.id.location_container, locationFragment);

        if (descriptionFragment == null)
            descriptionFragment = DescriptionRequestFragment.newInstance("Description (if any):");
        fragmentTransaction.replace(R.id.description_container, descriptionFragment);

        if (dateFragment == null)
            dateFragment = DateRequestFragment.newInstance(null, true);
        fragmentTransaction.replace(R.id.date_container, dateFragment);

        // Not necessary???
        if (timeFragment == null)
            timeFragment = TimeRequestFragment.newInstance(null, true);
        fragmentTransaction.replace(R.id.time_container, timeFragment);

        if (selectionFragment == null) {
            ArrayList<String> itemList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.request_tutoring_type)));
            selectionFragment = SelectionRequestFragment.newInstance(null, "Choose Tutoring Type:", itemList);
        }
        fragmentTransaction.replace(R.id.selection_container, selectionFragment);

        //pictureFragment = PictureRequestFragment.newInstance(findViewById(R.id.expanded_image));
        //fragmentTransaction.replace(R.id.picture_container, pictureFragment);

        fragmentTransaction.commit();

        ConstraintLayout tutorInformation = findViewById(R.id.tutor_info_view);

        courseCodeEdit = tutorInformation.findViewById(R.id.course_code_edit);
        setCourseCodeTextEdit(courseCodeEdit);


        participantSpinner = tutorInformation.findViewById(R.id.participant_spinner);
        setDropDownList(participantSpinner, getNumberStringArray(1, 9));

        firebaseAuth = FirebaseAuth.getInstance();

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
                //replyIntent.putExtra(getString(R.string.key_request_picture), pictureFragment.getInformationBitmap());

                replyIntent.putExtra(getString(R.string.key_request_course_code), courseCodeEdit.getText().toString());
                replyIntent.putExtra(getString(R.string.key_request_participant), Integer.valueOf((String)participantSpinner.getSelectedItem()));

                setResult(Activity.RESULT_OK, replyIntent);
                try {
                    TutoringFavor favor = new TutoringFavor();
                    favor.setEnquirer(firebaseAuth.getCurrentUser().getUid());
                    favor.setEnquirerName(firebaseAuth.getCurrentUser().getDisplayName());
                    favor.setTaskType(TaskType.TUTORING);
                    favor.setStatus(Status.OPEN);
                    favor.setLocation(new edu.cuhk.csci3310.project.model.LatLng(locationFragment.getInformationLocation()));
                    favor.setDescription(descriptionFragment.getInformationString());
                    favor.setStartDate(dateFragment.getInformationDate());
                    favor.setEndDate(dateFragment.getInformationDateEnd());
                    favor.setStartTime(timeFragment.getInformationTime());
                    favor.setEndTime(timeFragment.getInformationTimeEnd());
                    favor.setSelection(selectionFragment.getInformationStringList());
                    //favor.setPicture(pictureFragment.getInformationBitmap());
                    favor.setCourseCode(courseCodeEdit.getText().toString());
                    favor.setCourseParticipant(Integer.parseInt((String) participantSpinner.getSelectedItem()));
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentReference docRef = db.collection("users").document(firebaseAuth.getCurrentUser().getUid());
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                    String username = (String) document.getData().get("name");
                                    favor.setEnquirerName(username);
                                    try {
                                        Database.createNewFavor(favor);
                                    } catch(Exception e) {
                                        Log.d(TAG, e.getMessage());
                                    }
                                } else {
                                    Log.d(TAG, "No such document");
                                }
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });
                } catch(Exception e) {
                    Log.d(TAG, "onClick: " + e.getMessage());
                }
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
        fragmentManager.putFragment(outState, getResources().getResourceName(R.id.selection_container), selectionFragment);
        //fragmentManager.putFragment(outState, getResources().getResourceName(R.id.picture_container), pictureFragment);
        outState.putInt(getResources().getResourceName(R.id.participant_spinner), participantSpinner.getSelectedItemPosition());
    }

    @Override
    protected void onLoadInstanceState(@Nullable Bundle savedInstanceState){
        FragmentManager fragmentManager = getSupportFragmentManager();
        locationFragment = (LocationRequestFragment) fragmentManager.getFragment(savedInstanceState, getResources().getResourceName(R.id.location_container));
        descriptionFragment = (DescriptionRequestFragment) fragmentManager.getFragment(savedInstanceState, getResources().getResourceName(R.id.description_container));
        dateFragment = (DateRequestFragment) fragmentManager.getFragment(savedInstanceState, getResources().getResourceName(R.id.date_container));
        timeFragment = (TimeRequestFragment) fragmentManager.getFragment(savedInstanceState, getResources().getResourceName(R.id.time_container));
        selectionFragment = (SelectionRequestFragment) fragmentManager.getFragment(savedInstanceState, getResources().getResourceName(R.id.selection_container));
        //pictureFragment = (PictureRequestFragment) fragmentManager.getFragment(savedInstanceState, getResources().getResourceName(R.id.picture_container));
    }

    protected void onLoadInstanceStateLate(@Nullable Bundle savedInstanceState){
        if (savedInstanceState != null){
            courseCodeEdit.setText(savedInstanceState.getString(getResources().getResourceName(R.id.course_code_edit)));
            participantSpinner.setSelection(savedInstanceState.getInt(getResources().getResourceName(R.id.participant_spinner)));
        }
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