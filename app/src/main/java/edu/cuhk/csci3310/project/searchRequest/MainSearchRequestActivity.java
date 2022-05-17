package edu.cuhk.csci3310.project.searchRequest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.util.Calendar;

import edu.cuhk.csci3310.project.R;


public class MainSearchRequestActivity extends AppCompatActivity {
    private static final String TAG = "SearchRequestActivity";

    // reuse favor model for searching
    String favorType;
    EditText enquirerName;
    EditText startDateView;
    EditText endDateView;

    // use visibility to control option menu on screen
    // fragment is another option, but it is not necessary in the simple case
    View currentView; // variable to store current view
    View allView;
    View BorrowingView; String itemType; EditText itemNameView;
    View DiningView;
    View GatheringView;
    View MovingView;
    View TutoringView; String tutoringType; EditText courseCodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Log.d(TAG, "on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_search_rquest);

        // default to search for any favor, so give it a basic favor to pass to next activity
        favorType = "all";

        allView = findViewById(R.id.query_any);
        BorrowingView = findViewById(R.id.query_Borrowing);;
        DiningView = findViewById(R.id.query_Dining);
        GatheringView = findViewById(R.id.query_Gathering);
        MovingView = findViewById(R.id.query_Moving);
        TutoringView = findViewById(R.id.query_Tutoring);
        currentView = allView; // user looking at allView

        // enquirerName input edittext
        enquirerName = findViewById(R.id.query_name_input);

        // start date and end date input
        startDateView = findViewById(R.id.query_start_date);
        endDateView = findViewById(R.id.query_end_date);
        startDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                DatePickerDialog picker = new DatePickerDialog(MainSearchRequestActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                startDateView.setText(+ year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                            }
                        }, year, month, day);
                // set to cancel button to reset input to empty string
                picker.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_NEGATIVE) {
                            startDateView.setText("");
                        }
                    }
                });
                picker.show();
            }
        });
        endDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                DatePickerDialog picker = new DatePickerDialog(MainSearchRequestActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                endDateView.setText(+ year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                            }
                        }, year, month, day);
                picker.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_NEGATIVE) {
                            endDateView.setText("");
                        }
                    }
                });
                picker.show();
            }
        });

        // borrowing optional input
        Spinner borrowingspinner = (Spinner) findViewById(R.id.query_Borrowing_spinner);
        borrowingspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                itemType = (String) adapterView.getItemAtPosition(pos);
                // Log.d(TAG, "get item : " + item);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        ArrayAdapter<CharSequence> borrowingadapter = ArrayAdapter.createFromResource(this,
                R.array.request_borrowing_type, android.R.layout.simple_spinner_item);
        borrowingadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        borrowingspinner.setAdapter(borrowingadapter);

        itemNameView = findViewById(R.id.query_Borrowing_item_input);

        // Tutoring optional input

        Spinner Tutoringspinner = (Spinner) findViewById(R.id.query_Tutoring_spinner);
        Tutoringspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                tutoringType = (String) adapterView.getItemAtPosition(pos);
                // Log.d(TAG, "get item : " + item);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        ArrayAdapter<CharSequence> Tutoringadapter = ArrayAdapter.createFromResource(this,
                R.array.query_tutoring_type, android.R.layout.simple_spinner_item);
        Tutoringadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Tutoringspinner.setAdapter(Tutoringadapter);

        courseCodeView = findViewById(R.id.query_Tutoring_courseCode_input);

    }

    public void onTypeClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.radio_all:
                if (checked) {
                    changeView(currentView, allView); favorType = "all";
                }
                    break;
            case R.id.radio_Borrowing:
                if (checked) {
                    changeView(currentView, BorrowingView); favorType = "Borrowing";
                }
                    break;
            case R.id.radio_Dining:
                if (checked) {
                    changeView(currentView, DiningView); favorType = "Dining";
                }
                    break;
            case R.id.radio_Gathering:
                if (checked) {
                    changeView(currentView, GatheringView); favorType = "Gathering";
                }
                    break;
            case R.id.radio_Moving:
                if (checked) {
                    changeView(currentView, MovingView); favorType = "Moving";
                }
                    break;
            case R.id.radio_Tutoring:
                if (checked) {
                    changeView(currentView, TutoringView); favorType = "Tutoring";
                }
                    break;
        }
    }

    // sending ParcelableObject
    // reference: stack overflow question
    // https://stackoverflow.com/questions/2139134/how-to-send-an-object-from-one-android-activity-to-another-using-intents
    public void onQuerySubmit(View view) {
        Intent intent = new Intent(MainSearchRequestActivity.this, searchResultActivity.class);
        Bundle extras = new Bundle();
        extras.putString("favorType", favorType);
        // empty string are handled in query result
        extras.putString("enquirerName", enquirerName.getText().toString());
        extras.putString("startDate", startDateView.getText().toString());
        extras.putString("endDate", endDateView.getText().toString());
        switch(favorType){
            case "all":
                break;
            case "Borrowing":
                extras.putString("itemType", itemType);
                extras.putString("itemName", itemNameView.getText().toString());
                break;
            case "Dining":
                break;
            case "Gathering":
                break;
            case "Moving":
                break;
            case "Tutoring":
                extras.putString("tutoringType", tutoringType);
                extras.putString("courseCode", courseCodeView.getText().toString());
                Log.d(TAG, "tutoringType : " + tutoringType);
                Log.d(TAG, "courseCode : " + courseCodeView.getText().toString());
                break;
        }
        intent.putExtras(extras);
        startActivity(intent);
    }

    // swap visibility of two view
    private void changeView(View e1, View e2){
        e1.setVisibility(View.GONE);
        e2.setVisibility(View.VISIBLE);
        currentView = e2;
    }

}