package edu.cuhk.csci3310.project.searchRequest;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.util.Locale;

import edu.cuhk.csci3310.project.MainActivity;
import edu.cuhk.csci3310.project.R;
import edu.cuhk.csci3310.project.database.Status;
import edu.cuhk.csci3310.project.model.Favor;

public class MainSearchRequestActivity extends AppCompatActivity {
    private static final String TAG = "SearchRequestActivity";

    // reuse favor model for searching
    String favorType;
    final Calendar myCalendar= Calendar.getInstance();
    String selectedDate;
    // use visibility to control option menu on screen
    // fragment is another option, but it is not necessary in the simple case
    View currentView; // variable to store current view
    View allView;
    View BorrowingView; String itemType; EditText dateView;
    View DiningView; int participantNum; EditText query_Dining_date;
    View GatheringView;
    View MovingView;
    View TutoringView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_search_rquest);

        // default to search for any favor, so give it a basic favor to pass to next activity
        favorType = "all";

        allView = findViewById(R.id.query_any);
        BorrowingView = findViewById(R.id.query_Borrowing);;
        DiningView = findViewById(R.id.query_Dining);
        currentView = allView; // user looking at allView

        Spinner spinner = (Spinner) findViewById(R.id.query_Borrowing_spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                itemType = (String) adapterView.getItemAtPosition(pos);
                // Log.d(TAG, "get item : " + item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.request_borrowing_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // dateView = findViewById(R.id.query_date);
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };
        View.OnClickListener l = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(MainSearchRequestActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        };
        dateView = findViewById(R.id.query_date);
        query_Dining_date = findViewById(R.id.query_Dining_date);
        View array[] = new View[]{dateView, query_Dining_date };
        for (View view : array){
            view.setOnClickListener(l);
        }

        String myFormat="dd/MM/yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        selectedDate = dateFormat.format(myCalendar.getTime());
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
                    dateView.setText(selectedDate);
                    changeView(currentView, BorrowingView); favorType = "Borrowing";

                }
                    break;
            case R.id.radio_Dining:
                if (checked) {
                    query_Dining_date.setText(selectedDate);
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
        switch(favorType){
            case "all":
                break;
            case "Borrowing":
                extras.putString("itemType", itemType);
                extras.putString("date", selectedDate);
                //Log.d(TAG, "get date: " + dateView.getText());
                break;
            case "Dining":
                break;
            case "Gathering":
                break;
            case "Moving":
                break;
            case "Tutoring":
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

    private void updateLabel(){
        String myFormat="dd/MM/yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        String date = dateFormat.format(myCalendar.getTime());
        selectedDate = date;
        dateView.setText(selectedDate);
        query_Dining_date.setText(selectedDate);
    }
}