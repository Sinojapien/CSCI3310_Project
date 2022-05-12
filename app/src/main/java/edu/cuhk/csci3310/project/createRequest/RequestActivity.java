package edu.cuhk.csci3310.project.createRequest;

// Name: Yeung Chi Ho, SID: 1155126460

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import edu.cuhk.csci3310.project.R;
import edu.cuhk.csci3310.project.SelectionListAdapter;

public class RequestActivity extends AppCompatActivity {

    /* todo:
        Web location
        SavedInstanceState: Time/Date Dialog, bitmap for PictureRequestFragment (use ViewModel?), RequestMapActivity (Zoom)
        map of all restaurants / list of restaurants
        extend EnlargeImageFragment with recycler
        possible messaging function?
        http://www.res.cuhk.edu.hk/en-gb/general-information/program-codes
    */

    protected Animator currentAnimator;
    protected int shortAnimationDuration = 1;

    // https://stackoverflow.com/questions/215497/what-is-the-difference-between-public-protected-package-private-and-private-in
    protected SharedPreferences mPreferences;
    protected final String sharedPreferenceFile = "edu.cuhk.csci3310.project.request.activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferences = getSharedPreferences(sharedPreferenceFile, MODE_PRIVATE);
        if (savedInstanceState != null)
            onLoadInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() { super.onResume(); }

    @Override
    protected void onPause() {
        super.onPause();
        savePreference();
    }

    @Override
    protected void onStop() {
        super.onStop();
        savePreference();
    }

    protected void loadPreference(){
        // Restore preferences
        // mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        // currentRating = mPreferences.getInt(getKey(RATING_KEY), defaultRating);
        // nameEditText.setText(mPreferences.getString(getKey(NAME_KEY), name));
    }

    protected void savePreference(){
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        // preferencesEditor.putInt(RATING_KEY, currentRating);
        // preferencesEditor.putString(NAME_KEY, nameEditText.getText().toString());
        preferencesEditor.apply();
    }

    protected void resetPreference(){
        mPreferences.edit().clear().apply();
    }

    protected void onLoadInstanceState(@Nullable Bundle savedInstanceState){
        // https://stackoverflow.com/questions/15313598/how-to-correctly-save-instance-state-of-fragments-in-back-stack
        // https://stackoverflow.com/questions/14647810/easier-way-to-get-views-id-string-by-its-id-int
    }

    protected boolean isAllInformationFilled(){ return true; }

    public static String[] getNumberStringArray(int start, int end){
        int total = end - start + 1;
        String[] output = new String[total];
        for (int i=0; i<total; i++)
            output[i] = String.valueOf(start + i);
        return output;
    }

    public static long getTimeInMillis(int year, int month, int day){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTimeInMillis();
    }

    protected void readCSV(int rawResourceID){
        try {
            InputStream csvFile = getResources().openRawResource(rawResourceID);
            BufferedReader cvsFileReader = new BufferedReader(new InputStreamReader(csvFile));

            float totalLatitude = 0;
            float totalLongitude = 0;
            int count = 0;

            cvsFileReader.readLine(); // Skip 1st line
            while(cvsFileReader.ready()) {
                String[] line = cvsFileReader.readLine().split(",");
                float flowerLatitude = Float.parseFloat(line[2]);
                float flowerLongitude = Float.parseFloat(line[3]);

//                mImagePathList.addLast(mDrawableFilePath + line[0].substring(0, line[0].length() - 4));
//                mTitleList.addLast(line[1]);

//                mLatitudeList.addLast(flowerLatitude);
//                mLongitudeList.addLast(flowerLongitude);

                totalLatitude += flowerLatitude;
                totalLongitude += flowerLongitude;
                count++;
            }
//            centerPosition = new LatLng(totalLatitude / count, totalLongitude / count);

            cvsFileReader.close();
            csvFile.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void setRetrievePictureButtonView(Button buttonView, ActivityResultLauncher<Intent> activityResultLauncher){
        buttonView.setOnClickListener(new View.OnClickListener() {

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
                    activityResultLauncher.launch(pictureChooserIntent);
                } catch (ActivityNotFoundException e) {
                    // display error state to the user
                }
            }
        });
    }

    protected void setWordCountTextView(TextView textView){
        int descriptionMaxLength = getResources().getInteger(R.integer.request_description_max_length);
        textView.setText(getString(R.string.request_description_wordcount, 0, descriptionMaxLength));

        ((TextView) findViewById(R.id.description_edit)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // int testLength = charSequence.toString().length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textView.setText(getString(R.string.request_description_wordcount, charSequence.toString().length(), descriptionMaxLength));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    protected void setZoomableImageView(ImageView imageView, ImageView expandedImageView){
        // resize fragment
        // https://stackoverflow.com/questions/10699464/dynamic-resize-fragment-android
        // https://developer.android.com/guide/fragments/animate
        // https://developer.android.com/training/animation/zoom#java

        if (currentAnimator != null) {
            currentAnimator.cancel();
        }

        {
            // put it on RealRequestActivity?
            imageView.setImageDrawable(null);
            expandedImageView.setImageDrawable(null);
        }
        expandedImageView.setClickable(false);
        expandedImageView.setVisibility(View.INVISIBLE);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((ImageView) view).getDrawable() == null)
                    return;
                expandedImageView.setClickable(true);
                expandedImageView.setVisibility(View.VISIBLE);
                expandedImageView.setImageDrawable(((ImageView) view).getDrawable());
            }
        });
        if (!expandedImageView.hasOnClickListeners()) {
            expandedImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setClickable(false);
                    view.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    protected void setDatePickerEditText(TextView textView) {
        // https://www.techypid.com/datepicker-dialog-click-on-edittext-in-android/
        // https://xken831.pixnet.net/blog/post/460794230-%5Bandroid%5D-%E6%8C%91%E9%81%B8%E6%99%82%E9%96%93%E5%85%83%E4%BB%B6-datepickerdialog-%E4%BD%BF%E7%94%A8%E6%96%B9%E5%BC%8F
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        textView.setCursorVisible(false);
        textView.setFocusable(false);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        String dateString = getString(R.string.request_date, i2, i1, i);
                        // String dateString = i + "-" + (i1 + 1) + "-" + i2;
                        textView.setText(dateString);
                    }
                }, year, month, day).show();
            }
        });
    }

    protected void setTimePickerEditText(TextView textView){
        // https://www.journaldev.com/9976/android-date-time-picker-dialog
        // https://stackoverflow.com/questions/41829665/android-studioedittext-editable-is-deprecated-how-to-use-inputtype
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        textView.setCursorVisible(false);
        textView.setFocusable(false);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        String timeString = getString(R.string.request_time, i, i1);
                        // String timeString = i + ":" + i1;
                        textView.setText(timeString);
                    }
                }, hour, minute, true).show();
            }
        });
    }

    protected void setSelectionView(View view, RecyclerView recyclerView, ArrayList<String> items){
        // https://www.geeksforgeeks.org/alert-dialog-with-multipleitemselection-in-android/
        // https://www.geeksforgeeks.org/alert-dialog-with-singleitemselection-in-android/
        // https://developer.android.com/reference/kotlin/androidx/appcompat/app/AlertDialog.Builder
        // https://developer.android.com/guide/topics/ui/dialogs#java

        //SelectionListAdapter adapter = (SelectionListAdapter) recyclerView.getAdapter();
        //assert adapter != null;
        SelectionListAdapter adapter = new SelectionListAdapter(view.getContext(), null, 0);
        recyclerView.setAdapter(adapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 3);
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        //tutoringTypeRecycler.setHasFixedSize(true);

        final boolean[] checkedIndex = new boolean[items.size()];

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Choose Tutoring Type:");
                builder.setIcon(R.drawable.common_google_signin_btn_icon_light);
                builder.setMultiChoiceItems(items.toArray(new String[0]), checkedIndex, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                        checkedIndex[i] = isChecked;
                    }
                });

                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ArrayList<String> checkedItems = new ArrayList<>();
                        for(int p=0; p<checkedIndex.length;p++){
                            if (checkedIndex[p])
                                checkedItems.add(items.get(p));
                        }
                        adapter.clearItem();
                        adapter.addItem(checkedItems);
                    }
                });

                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.setNeutralButton("CLEAR ALL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Arrays.fill(checkedIndex, false);
                        // adapter.clearItem();
                    }
                });

                builder.show();
            }
        });
    }

    protected void setCourseCodeTextEdit(TextView textView){
        textView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);

        // InputFilter[] inputFilters = textView.getFilters();
        // textView.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        textView.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // https://www.geeksforgeeks.org/restrict-edittext-input-to-some-special-characters-in-android/
                // https://stackoverflow.com/questions/7300490/set-edittext-digits-programmatically
                int currentLength = textView.getText().toString().length();
                if (currentLength < 4){
                    textView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                }else{
                    textView.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    protected void setDropDownList(Spinner spinner, String[] itemArray){
        // https://material.io/components/menus/android#using-menus
        // https://stackoverflow.com/questions/13377361/how-to-create-a-drop-down-list
        // https://stackoverflow.com/questions/36857555/how-to-customize-checkboxes-in-multi-select-spinner-in-android
        ArrayAdapter<String> adapter = new ArrayAdapter<>(spinner.getContext(), android.R.layout.simple_spinner_dropdown_item, itemArray);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

}