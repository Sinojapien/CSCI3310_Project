package edu.cuhk.csci3310.project.createRequest;

// Name: Yeung Chi Ho, SID: 1155126460

import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import edu.cuhk.csci3310.project.R;

public class MovingRequestActivity extends RequestActivity {

    LocationRequestFragment startLocationFragment;
    LocationRequestFragment endLocationFragment;
    DateRequestFragment dateFragment;
    TimeRequestFragment timeFragment;
    DescriptionRequestFragment descriptionFragment;
    PictureRequestFragment pictureFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_moving);

        setTitle("Moving Request");

        // Restore preferences
//        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
//        currentRating = mPreferences.getInt(getKey(RATING_KEY), defaultRating);
//        nameEditText.setText(mPreferences.getString(getKey(NAME_KEY), name));

//        restaurantTextView.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View v) {
//                String url = OPENRICE_URL + restaurantTextView.getText().toString();
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                startActivity(intent);
//            }
//        });

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        startLocationFragment = LocationRequestFragment.newInstance("Start Location", getMapBoundary(new LatLng(22.418014,	114.207259), 0.075));
        fragmentTransaction.replace(R.id.start_location_container, startLocationFragment);

        endLocationFragment = LocationRequestFragment.newInstance("End Location", getMapBoundary(new LatLng(22.418014,	114.207259), 0.075));
        fragmentTransaction.replace(R.id.end_location_container, endLocationFragment);

        dateFragment = DateRequestFragment.newInstance(false);
        fragmentTransaction.replace(R.id.date_container, dateFragment);

        timeFragment = TimeRequestFragment.newInstance(false);
        fragmentTransaction.replace(R.id.time_container, timeFragment);

        descriptionFragment = DescriptionRequestFragment.newInstance("Description (if any):");
        fragmentTransaction.replace(R.id.description_container, descriptionFragment);

        pictureFragment = PictureRequestFragment.newInstance(findViewById(R.id.expanded_image));
        fragmentTransaction.replace(R.id.picture_container, pictureFragment);

        fragmentTransaction.commit();

        findViewById(R.id.post_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAllInformationFilled()){
                    Toast.makeText(view.getContext(), "Please fill in all required info.", Toast.LENGTH_SHORT).show();
                    return;
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
    protected boolean isAllInformationFilled(){
        boolean isAllFilled = true;

        isAllFilled &= startLocationFragment.isFilled();
        isAllFilled &= endLocationFragment.isFilled();
        isAllFilled &= dateFragment.isFilled();
        isAllFilled &= timeFragment.isFilled();
        isAllFilled &= descriptionFragment.isFilled();

        return isAllFilled;
    }

}