package edu.cuhk.csci3310.project;

// Name: Yeung Chi Ho, SID: 1155126460

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class MovingRequestActivity extends AppCompatActivity {

    private SharedPreferences mPreferences;

    private TextView valueRatingTextView;
    private EditText nameEditText;
    private TextView restaurantTextView;
    private ImageView largeImageView;

    private final String sharedPrefFile = "edu.cuhk.csci3310.project";
    private final String MOVING_KEY = "PROJECT/REQUEST/MOVING";

    ActivityResultLauncher<Intent> activityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // https://developer.android.com/training/basics/intents/result
                        Intent data = result.getData();
                        if (data != null) {
                            String resultData = data.getStringExtra("TEST");
                            Log.w("TEST", resultData);
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> cameraActivityLauncher = registerForActivityResult(
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
                            ((ImageView) findViewById(R.id.photo_view)).setImageBitmap(imageBitmap);
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_moving);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        // https://stackoverflow.com/questions/10699464/dynamic-resize-fragment-android
        // https://developer.android.com/guide/fragments/animate
        // https://developer.android.com/training/animation/zoom#java
        // resize fragment

        // Restore preferences
//        currentRating = mPreferences.getInt(getKey(RATING_KEY), defaultRating);
//        nameEditText.setText(mPreferences.getString(getKey(NAME_KEY), name));

//        restaurantTextView.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View v) {
//                String url = OPENRICE_URL + restaurantTextView.getText().toString();
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                startActivity(intent);
//            }
//        });

        findViewById(R.id.start_map_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng location = new LatLng(22.418014,	114.207259);

                Intent intent = new Intent(MovingRequestActivity.this, RequestMapActivity.class);
                intent.putExtra(getString(R.string.key_map_boundary), getMapBoundary(location, 0.005));
                intent.putExtra(getString(R.string.key_map_location), location);
                intent.putExtra(getString(R.string.key_map_title), "Start Location");
                intent.putExtra(getString(R.string.key_map_icon), BitmapDescriptorFactory.HUE_RED);

                // startActivity(intent);
                activityLauncher.launch(intent);
            }
        });

        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
            findViewById(R.id.photo_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent getPictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    Intent getPictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                    getPictureIntent.setType("image/*");
//                    getPictureIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    try {
//                        cameraActivityLauncher.launch(Intent.createChooser(getPictureIntent, "Select Picture"));
                        cameraActivityLauncher.launch(getPictureIntent);
                    } catch (ActivityNotFoundException e) {
                        // display error state to the user
                    }
                }
            });
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        savePreference();
    }

    /*
    @Override
    protected void onResume() {
        super.onResume();
    }
    */

    @Override
    protected void onPause() {
        super.onPause();
        savePreference();
    }

    protected void savePreference(){
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
//        preferencesEditor.putInt(RATING_KEY, currentRating);
//        preferencesEditor.putString(NAME_KEY, nameEditText.getText().toString());
        preferencesEditor.apply();
    }

    protected void resetPreference(){
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.clear();
        preferencesEditor.apply();
    }

    private LatLngBounds getMapBoundary(LatLng center, double offset){
        LatLng boundaryNE = new LatLng(center.latitude - offset,	center.longitude - offset);
        LatLng boundarySW = new LatLng(center.latitude + offset,	center.longitude + offset);
        return new LatLngBounds(boundaryNE, boundarySW);
    }

}