package edu.cuhk.csci3310.project.requestDetails;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import edu.cuhk.csci3310.project.R;
import edu.cuhk.csci3310.project.createRequest.RequestActivity;
import edu.cuhk.csci3310.project.database.Status;
import edu.cuhk.csci3310.project.model.BorrowingFavor;
import edu.cuhk.csci3310.project.model.DiningFavor;
import edu.cuhk.csci3310.project.model.Favor;
import edu.cuhk.csci3310.project.model.GatheringFavor;
import edu.cuhk.csci3310.project.model.MovingFavor;
import edu.cuhk.csci3310.project.model.TutoringFavor;
import edu.cuhk.csci3310.project.requestDetails.actionFragments.FavorActionFragment;
import edu.cuhk.csci3310.project.requestDetails.actionFragments.RequestActionFragment;
import edu.cuhk.csci3310.project.requestDetails.actionFragments.TaskActionFragment;
import edu.cuhk.csci3310.project.requestDetails.favorFragments.BorrowingFragment;
import edu.cuhk.csci3310.project.requestDetails.favorFragments.DiningFragment;
import edu.cuhk.csci3310.project.requestDetails.favorFragments.GatheringFragment;
import edu.cuhk.csci3310.project.requestDetails.favorFragments.MovingFragment;
import edu.cuhk.csci3310.project.requestDetails.favorFragments.TutoringFragment;


public class RequestDetailsActivity extends FragmentActivity {

    private static final String TAG = "RequestDetailsActivity";

    Favor favor;

    // View objects
    TextView requestTypeTV;
    TextView statusTV;
    TextView enquirerTV;
    TextView accepterTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_request_details);

        Intent intent = getIntent();
        favor = intent.getParcelableExtra("FAVOR");
        instantiateTextFields();
        fillTextFields();
        showFavorFragment(favor.getTaskTypeString());
        showActionFragment(intent.getStringExtra("TYPE"), favor.getStatusString());
    }

    private void instantiateTextFields() {
        requestTypeTV = findViewById(R.id.request_type_TV);
        statusTV = findViewById(R.id.status_TV);
        enquirerTV = findViewById(R.id.enquirer_TV);
        accepterTV = findViewById(R.id.accpter_TV);
    }

    private void fillTextFields() {
        if(favor.getTaskTypeString() != null) {
            requestTypeTV.setText(favor.getTaskTypeString());
        }
        if(favor.getStatusString() != null) {
            statusTV.setText(favor.getStatusString());
        }
        if(favor.getEnquirerName() != null) {
            enquirerTV.setText(favor.getEnquirerName());
        }
        if(favor.getAccepter() != null) {
            accepterTV.setText(favor.getAccepter());
            // Add implementation to display username of accepter
        }
    }

    public void showFavorFragment(String taskType) {
        Fragment fragment;
        switch(taskType) {
            case "Moving":
                fragment = MovingFragment.newInstance((MovingFavor) favor); break;
            case "Tutoring":
                fragment = TutoringFragment.newInstance((TutoringFavor) favor); break;
            case "Dining":
                fragment = DiningFragment.newInstance((DiningFavor) favor); break;
            case "Gathering":
                fragment = GatheringFragment.newInstance((GatheringFavor) favor); break;
            case "Borrowing":
                fragment = BorrowingFragment.newInstance((BorrowingFavor) favor); break;
            default:
                return;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.favorFragmentContainer, fragment).commit();
    }

    public void showActionFragment(String type, String status) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch(type) {
            case "REQUEST":
                if (status == "Active") {
                    ft.add(R.id.actionFragmentContainer, new RequestActionFragment()).commit();
                }
                break;
            case "TASK":
                ft.add(R.id.actionFragmentContainer, new TaskActionFragment()).commit(); break;
            case "FAVOR":
                ft.add(R.id.actionFragmentContainer, new FavorActionFragment()).commit(); break;
        }
    }

}