package edu.cuhk.csci3310.project;

// Name: Yeung Chi Ho, SID: 1155126460

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

public class MovingRequestActivity extends RequestActivity {

    private TextView startLocationEditText;
    private TextView descriptionTextView;
    private ImageView photoImageView;

    private LatLng requestStartLocation;
    private LatLng requestDestination;
    private Bitmap requestPicture;  // Drawable not parsable

    private final ActivityResultLauncher<Intent> startMapActivityLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // https://developer.android.com/training/basics/intents/result
                    Intent data = result.getData();
                    if (data != null) {
                        Bundle dataExtra = data.getExtras();
                        requestStartLocation = (LatLng) dataExtra.get(getString(R.string.key_map_location));
                        startLocationEditText.setText((String) dataExtra.get(getString(R.string.key_map_title)));;
                    }
                }
            }
        });

    private final ActivityResultLauncher<Intent> pictureActivityLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                // https://developer.android.com/training/camera/photobasics#java
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        // data.getClipData();
                        Bundle extras = data.getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        requestPicture = imageBitmap;
                        photoImageView.setImageBitmap(imageBitmap);
                    }
                }
            }
        });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_moving);

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

        startLocationEditText = (TextView) findViewById(R.id.location_edit);
        startLocationEditText.setText("Start Location");
        descriptionTextView = (TextView) findViewById(R.id.description_edit);

        findViewById(R.id.location_map_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng location = new LatLng(22.418014,	114.207259);

                Intent intent = new Intent(MovingRequestActivity.this, RequestMapActivity.class);
                intent.putExtra(getString(R.string.key_map_boundary), getMapBoundary(location, 0.075));
                // intent.putExtra(getString(R.string.key_map_location), location);
                intent.putExtra(getString(R.string.key_map_title), startLocationEditText.getText().toString());
                intent.putExtra(getString(R.string.key_map_icon), BitmapDescriptorFactory.HUE_RED);

                startMapActivityLauncher.launch(intent);
            }
        });

        photoImageView = (ImageView) findViewById(R.id.photo_image);
        setRetrievePictureButtonView(findViewById(R.id.photo_button), pictureActivityLauncher);
        setZoomableImageView(photoImageView, findViewById(R.id.expanded_image));

        setWordCountTextView(findViewById(R.id.wordcount_text));

        TextView dateTextEdit = findViewById(R.id.date_edit);
        setDatePickerEditText(dateTextEdit);

        TextView timeTextEdit = findViewById(R.id.time_edit);
        setTimePickerEditText(timeTextEdit);

        findViewById(R.id.post_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isAllInformationFilled = true;
                isAllInformationFilled &= requestStartLocation != null;
                isAllInformationFilled &= requestDestination != null;
                isAllInformationFilled &= dateTextEdit.getText().toString().length() > 0;
                isAllInformationFilled &= timeTextEdit.getText().toString().length() > 0;
                if (!isAllInformationFilled){
                    Toast.makeText(view.getContext(), "Please fill in all required info.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent replyIntent = new Intent();
                replyIntent.putExtra(getString(R.string.key_request_type), getResources().getInteger(R.integer.request_type_moving));
                replyIntent.putExtra(getString(R.string.key_request_start_location), requestStartLocation);
                replyIntent.putExtra(getString(R.string.key_request_destination), requestDestination);
                replyIntent.putExtra(getString(R.string.key_request_description), descriptionTextView.getText().toString());
                replyIntent.putExtra(getString(R.string.key_request_picture), requestPicture);
                replyIntent.putExtra(getString(R.string.key_request_date), dateTextEdit.getText().toString());
                replyIntent.putExtra(getString(R.string.key_request_time), timeTextEdit.getText().toString());
                setResult(Activity.RESULT_OK, replyIntent);
                finish();
            }
        });

    }

}