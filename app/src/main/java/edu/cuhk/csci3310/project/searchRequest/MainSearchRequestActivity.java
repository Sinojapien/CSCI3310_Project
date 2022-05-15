package edu.cuhk.csci3310.project.searchRequest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import edu.cuhk.csci3310.project.R;
import edu.cuhk.csci3310.project.database.Status;
import edu.cuhk.csci3310.project.model.Favor;
import edu.cuhk.csci3310.project.requestDetails.RequestDetailsActivity;

public class MainSearchRequestActivity extends AppCompatActivity {
    private static final String TAG = "SearchRequestActivity";

    // reuse favor model for searching
    Favor favor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_search_rquest);

        // default to search for any favor, so give it a basic favor to pass to next activity
        favor = new Favor();
        favor.setStatus(Status.OPEN); // status will always be open when searching favor
    }

    public void onTypeClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.radio_all:
                if (checked)
                    break;
            case R.id.radio_Borrowing:
                if (checked)
                    break;
            case R.id.radio_Dining:
                if (checked)
                    break;
            case R.id.radio_Gathering:
                if (checked)
                    break;
            case R.id.radio_Moving:
                if (checked)
                    break;
            case R.id.radio_Tutoring:
                if (checked)
                    break;
        }

    }

    // sending ParcelableObject
    // reference: stack overflow question
    // https://stackoverflow.com/questions/2139134/how-to-send-an-object-from-one-android-activity-to-another-using-intents
    public void onQuerySubmit(View view) {
        Intent intent = new Intent(MainSearchRequestActivity.this, searchResultActivity.class);
        intent.putExtra("favor", favor);
        startActivity(intent);
    }
}