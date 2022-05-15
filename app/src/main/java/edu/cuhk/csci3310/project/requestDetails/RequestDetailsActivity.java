package edu.cuhk.csci3310.project.requestDetails;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import edu.cuhk.csci3310.project.R;
import edu.cuhk.csci3310.project.requestDetails.favorFragments.MovingFragment;


public class RequestDetailsActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_request_details);

        MovingFragment movingFragment = new MovingFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.favorFragmentContainer, movingFragment).commit();
    }
}