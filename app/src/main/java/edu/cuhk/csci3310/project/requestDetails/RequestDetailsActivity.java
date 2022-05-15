package edu.cuhk.csci3310.project.requestDetails;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import edu.cuhk.csci3310.project.R;
import edu.cuhk.csci3310.project.createRequest.RequestActivity;
import edu.cuhk.csci3310.project.model.Favor;
import edu.cuhk.csci3310.project.requestDetails.actionFragments.RequestActionFragment;
import edu.cuhk.csci3310.project.requestDetails.favorFragments.DiningFragment;
import edu.cuhk.csci3310.project.requestDetails.favorFragments.MovingFragment;
import edu.cuhk.csci3310.project.requestDetails.favorFragments.TutoringFragment;


public class RequestDetailsActivity extends FragmentActivity {

    Favor favor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_request_details);



        DiningFragment   movingFragment = new DiningFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.favorFragmentContainer, movingFragment).commit();

        RequestActionFragment actionFragment = new RequestActionFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.actionFragmentContainer, actionFragment).commit();
    }
}