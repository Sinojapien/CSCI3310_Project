package edu.cuhk.csci3310.project;

// Name: Yeung Chi Ho, SID: 1155126460

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;

public class MovingRequestActivity extends AppCompatActivity {

    private SharedPreferences mPreferences;

    private TextView valueRatingTextView;
    private EditText nameEditText;
    private TextView descriptionTextView;
    private TextView wordcountTextView;
    private ImageView photoView;

    private LatLng requestStartLocation;
    private LatLng requestDestination;
    // private String requestDescription;
    private Bitmap requestPicture;  // Drawable not parsable

    private Animator currentAnimator;
    private int shortAnimationDuration = 1;

    private final String sharedPrefFile = "edu.cuhk.csci3310.project";

    ActivityResultLauncher<Intent> startMapActivityLauncher = registerForActivityResult(
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
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> pictureActivityLauncher = registerForActivityResult(
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
                            ((ImageView) findViewById(R.id.photo_image)).setImageBitmap(imageBitmap);
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_moving);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

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

        descriptionTextView = (TextView) findViewById(R.id.description_edit);

        findViewById(R.id.start_map_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng location = new LatLng(22.418014,	114.207259);

                Intent intent = new Intent(MovingRequestActivity.this, RequestMapActivity.class);
                intent.putExtra(getString(R.string.key_map_boundary), getMapBoundary(location, 0.01));
                // intent.putExtra(getString(R.string.key_map_location), location);
                intent.putExtra(getString(R.string.key_map_title), "Start Location");
                intent.putExtra(getString(R.string.key_map_icon), BitmapDescriptorFactory.HUE_RED);

                startMapActivityLauncher.launch(intent);
            }
        });

        findViewById(R.id.photo_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // https://stackoverflow.com/questions/25524617/intent-to-choose-between-the-camera-or-the-gallery-in-android
                Intent pictureChooserIntent = new Intent(Intent.ACTION_CHOOSER);
                ArrayList<Intent> intentArrayList = new ArrayList<Intent>();

                Intent getPictureIntent = new Intent(Intent.ACTION_PICK);
                getPictureIntent.setType("image/*");
                // getPictureIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                pictureChooserIntent.putExtra(Intent.EXTRA_INTENT, getPictureIntent);
                pictureChooserIntent.putExtra(Intent.EXTRA_TITLE, "Select from:");

                if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intentArrayList.add(cameraIntent);
                }

                pictureChooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArrayList.toArray(new Intent[0]));
                intentArrayList.clear();

                try {
                    // pictureActivityLauncher.launch(Intent.createChooser(getPictureIntent, "Select Picture"));
                    // pictureActivityLauncher.launch(getPictureIntent);
                    pictureActivityLauncher.launch(pictureChooserIntent);
                } catch (ActivityNotFoundException e) {
                    // display error state to the user
                }
            }
        });

        //
        ImageView photoImageView = (ImageView) findViewById(R.id.photo_image);
        ImageView expandedImageView = (ImageView) findViewById(R.id.expanded_image);
        photoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandedImageView.setClickable(true);
                expandedImageView.setVisibility(View.VISIBLE);
                zoomImage((ImageView) view, expandedImageView);
            }
        });
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setClickable(false);
                view.setVisibility(View.INVISIBLE);
            }
        });

        int descriptionMaxLength = getResources().getInteger(R.integer.request_description_max_length);
        wordcountTextView = findViewById(R.id.wordcount_text);
        wordcountTextView.setText(getString(R.string.request_description_wordcount, 0, descriptionMaxLength));
        ((TextView) findViewById(R.id.description_edit)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // int testLength = charSequence.toString().length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                wordcountTextView.setText(getString(R.string.request_description_wordcount, charSequence.toString().length(), descriptionMaxLength));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        findViewById(R.id.post_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                replyIntent.putExtra(getString(R.string.key_request_start_location), requestStartLocation);
                replyIntent.putExtra(getString(R.string.key_request_destination), requestDestination);
                replyIntent.putExtra(getString(R.string.key_request_description), descriptionTextView.getText().toString());
                replyIntent.putExtra(getString(R.string.key_request_picture), requestPicture);
                setResult(Activity.RESULT_OK, replyIntent);
                finish();
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        savePreference();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

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

    private void zoomImage(ImageView imageView, ImageView expandedImageView){
        // resize fragment
        // https://stackoverflow.com/questions/10699464/dynamic-resize-fragment-android
        // https://developer.android.com/guide/fragments/animate
        // https://developer.android.com/training/animation/zoom#java

        if (currentAnimator != null) {
            currentAnimator.cancel();
        }
        expandedImageView.setImageDrawable(imageView.getDrawable());
    }

}