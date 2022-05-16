package edu.cuhk.csci3310.project.requestDetails.actionFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import edu.cuhk.csci3310.project.R;
import edu.cuhk.csci3310.project.model.Favor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RequestActionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequestActionFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_FAVOR = "favorParam";

    // View attributes
    Button taskCompletedBtn;

    private Favor favor;

    private View.OnClickListener listener;

    public RequestActionFragment() {
        // Required empty public constructor
    }

    public static RequestActionFragment newInstance(Favor favor) {
        RequestActionFragment fragment = new RequestActionFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_FAVOR, favor);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            favor = getArguments().getParcelable(ARG_FAVOR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request_action, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Instantiate button
        taskCompletedBtn = view.findViewById(R.id.task_completed_btn);
        if(listener != null) {
            taskCompletedBtn.setOnClickListener(listener);
        }
    }

    public void setButtonClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }
}