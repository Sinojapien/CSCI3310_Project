package edu.cuhk.csci3310.project.createRequest;

// Name: Yeung Chi Ho, SID: 1155126460

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import edu.cuhk.csci3310.project.R;
import edu.cuhk.csci3310.project.database.Database;
import edu.cuhk.csci3310.project.database.Status;
import edu.cuhk.csci3310.project.database.TaskType;
import edu.cuhk.csci3310.project.model.MovingFavor;

public class MovingRequestActivity extends RequestActivity {

    LocationRequestFragment startLocationFragment;
    LocationRequestFragment endLocationFragment;
    DateRequestFragment dateFragment;
    TimeRequestFragment timeFragment;
    DescriptionRequestFragment descriptionFragment;
    PictureRequestFragment pictureFragment;
    FirebaseAuth firebaseAuth;

    private static final String TAG = "MovingRequestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_moving);

        firebaseAuth = FirebaseAuth.getInstance();

        setTitle("Moving Request");

//        restaurantTextView.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View v) {
//                String url = OPENRICE_URL + restaurantTextView.getText().toString();
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                startActivity(intent);
//            }
//        });

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (startLocationFragment == null)
            //startLocationFragment = LocationRequestFragment.newInstance("Start Location:", getMapBoundary(new LatLng(22.418014,	114.207259), 0.075));
            startLocationFragment = LocationRequestFragment.newInstance("Start Location:", R.array.location_cuhk);
        fragmentTransaction.replace(R.id.start_location_container, startLocationFragment);

        if (endLocationFragment == null)
            //endLocationFragment = LocationRequestFragment.newInstance("End Location:", getMapBoundary(new LatLng(22.418014,	114.207259), 0.075));
            endLocationFragment = LocationRequestFragment.newInstance("End Location:", R.array.location_cuhk);
        fragmentTransaction.replace(R.id.end_location_container, endLocationFragment);

        if (dateFragment == null)
            dateFragment = DateRequestFragment.newInstance(null, false);
        fragmentTransaction.replace(R.id.date_container, dateFragment);

        if (timeFragment == null)
            timeFragment = TimeRequestFragment.newInstance(null, false);
        fragmentTransaction.replace(R.id.time_container, timeFragment);

        if (descriptionFragment == null)
            descriptionFragment = DescriptionRequestFragment.newInstance("Description (if any):");
        fragmentTransaction.replace(R.id.description_container, descriptionFragment);

        if (pictureFragment == null)
            pictureFragment = PictureRequestFragment.newInstance(findViewById(R.id.expanded_image));
        fragmentTransaction.replace(R.id.picture_container, pictureFragment);

        fragmentTransaction.commit();

        findViewById(R.id.post_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAllInformationFilled()) {
                    Toast.makeText(view.getContext(), "Please fill in all required info.", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    MovingFavor favor = new MovingFavor();
                    favor.setTaskType(TaskType.MOVING);
                    favor.setEnquirer(firebaseAuth.getCurrentUser().getUid());
                    favor.setDescription(descriptionFragment.getInformationString());
                    favor.setStatus(Status.OPEN);
                    favor.setStartLoc(new edu.cuhk.csci3310.project.model.LatLng(startLocationFragment.getInformationLocation()));
                    favor.setEndLoc(new edu.cuhk.csci3310.project.model.LatLng(endLocationFragment.getInformationLocation()));
                    favor.setDate(dateFragment.getInformationDate());
                    favor.setTime(timeFragment.getInformationTime());
                    favor.setPhoto(pictureFragment.getInformationBitmap());
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
                                        Database.createNewFavorAndSaveImage(favor, pictureFragment.getInformationBitmap());
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
                } catch (Exception e) {
                    Log.d(TAG, "onClick: " + e.getMessage());
                }
                Intent replyIntent = new Intent();
                replyIntent.putExtra(getString(R.string.key_request_type), getResources().getInteger(R.integer.request_type_moving));
                replyIntent.putExtra(getString(R.string.key_request_start_location), startLocationFragment.getInformationLocation());
                //replyIntent.putExtra(getString(R.string.key_request_location_string), startLocationFragment.getInformationString());
                replyIntent.putExtra(getString(R.string.key_request_destination), endLocationFragment.getInformationLocation());
                //replyIntent.putExtra(getString(R.string.key_request_location_string), endLocationFragment.getInformationString());
                replyIntent.putExtra(getString(R.string.key_request_date), dateFragment.getInformationDate());
                replyIntent.putExtra(getString(R.string.key_request_time), timeFragment.getInformationTime());
                replyIntent.putExtra(getString(R.string.key_request_description), descriptionFragment.getInformationString());
                replyIntent.putExtra(getString(R.string.key_request_picture), pictureFragment.getInformationBitmap());
                setResult(Activity.RESULT_OK, replyIntent);
                finish();
            }
        });

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.putFragment(outState, getResources().getResourceName(R.id.start_location_container), startLocationFragment);
        fragmentManager.putFragment(outState, getResources().getResourceName(R.id.end_location_container), endLocationFragment);
        fragmentManager.putFragment(outState, getResources().getResourceName(R.id.date_container), dateFragment);
        fragmentManager.putFragment(outState, getResources().getResourceName(R.id.time_container), timeFragment);
        fragmentManager.putFragment(outState, getResources().getResourceName(R.id.description_container), descriptionFragment);
        fragmentManager.putFragment(outState, getResources().getResourceName(R.id.picture_container), pictureFragment);
    }

    @Override
    protected void onLoadInstanceState(@Nullable Bundle savedInstanceState) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        startLocationFragment = (LocationRequestFragment) fragmentManager.getFragment(savedInstanceState, getResources().getResourceName(R.id.start_location_container));
        endLocationFragment = (LocationRequestFragment) fragmentManager.getFragment(savedInstanceState, getResources().getResourceName(R.id.end_location_container));
        dateFragment = (DateRequestFragment) fragmentManager.getFragment(savedInstanceState, getResources().getResourceName(R.id.date_container));
        timeFragment = (TimeRequestFragment) fragmentManager.getFragment(savedInstanceState, getResources().getResourceName(R.id.time_container));
        descriptionFragment = (DescriptionRequestFragment) fragmentManager.getFragment(savedInstanceState, getResources().getResourceName(R.id.description_container));
        pictureFragment = (PictureRequestFragment) fragmentManager.getFragment(savedInstanceState, getResources().getResourceName(R.id.picture_container));
    }

    @Override
    protected boolean isAllInformationFilled() {
        boolean isAllFilled = true;

        isAllFilled &= startLocationFragment.isFilled();
        isAllFilled &= endLocationFragment.isFilled();
        isAllFilled &= dateFragment.isFilled();
        isAllFilled &= timeFragment.isFilled();
        isAllFilled &= descriptionFragment.isFilled();

        return isAllFilled;
    }

}