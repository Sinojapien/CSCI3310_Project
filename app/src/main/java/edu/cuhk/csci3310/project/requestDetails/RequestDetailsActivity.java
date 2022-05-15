package edu.cuhk.csci3310.project.requestDetails;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_request_details);

        Intent intent = getIntent();
        // Favor favor = getFavorObjectFromIntent(intent);
        favor = intent.getParcelableExtra("FAVOR");
        showFavorFragment(favor.getTaskTypeString());
        showActionFragment(intent.getStringExtra("TYPE"));
    }

    public void showFavorFragment(String taskType) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch(taskType) {
            case "Moving":
                ft.add(R.id.favorFragmentContainer, new MovingFragment()).commit(); break;
            case "Tutoring":
                ft.add(R.id.favorFragmentContainer, new TutoringFragment()).commit(); break;
            case "Dining":
                ft.add(R.id.favorFragmentContainer, new DiningFragment()).commit(); break;
            case "Gathering":
                ft.add(R.id.favorFragmentContainer, new GatheringFragment()).commit(); break;
            case "Borrowing":
                ft.add(R.id.favorFragmentContainer, new BorrowingFragment()).commit(); break;
        }
    }

    public void showActionFragment(String type) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch(type) {
            case "REQUEST":
                ft.add(R.id.actionFragmentContainer, new RequestActionFragment()).commit(); break;
            case "TASK":
                ft.add(R.id.actionFragmentContainer, new TaskActionFragment()).commit(); break;
            case "FAVOR":
                ft.add(R.id.actionFragmentContainer, new FavorActionFragment()).commit(); break;
        }
    }

}