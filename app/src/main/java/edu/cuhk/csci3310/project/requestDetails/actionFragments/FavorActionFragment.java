package edu.cuhk.csci3310.project.requestDetails.actionFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.firestore.FirebaseFirestore;

import edu.cuhk.csci3310.project.R;
import edu.cuhk.csci3310.project.model.Favor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavorActionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavorActionFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_FAVOR = "favorParam";
    private static final String ARG_USER = "userParam";

    // View attributes
    Button assignFulfillerBtn;

    private Favor favor;
    private String userId;

    // Listener
    private View.OnClickListener listener;

    public FavorActionFragment() {
        // Required empty public constructor
    }

    public static FavorActionFragment newInstance(Favor favor, String userId) {
        FavorActionFragment fragment = new FavorActionFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_FAVOR, favor);
        args.putString(ARG_USER, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            favor = getArguments().getParcelable(ARG_FAVOR);
            userId = getArguments().getString(ARG_USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favor_action, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Instantiate button
        assignFulfillerBtn = view.findViewById(R.id.assign_fulfiller_Btn);
        if(listener != null) {
            assignFulfillerBtn.setOnClickListener(listener);
        }
    }

    public void setButtonClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }
}