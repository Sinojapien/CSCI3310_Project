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
 * Use the {@link TaskActionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskActionFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_FAVOR = "favorParam";

    // View attributes
    Button removeFulfillerBtn;

    private Favor favor;

    private View.OnClickListener listener;

    public TaskActionFragment() {
        // Required empty public constructor
    }

    public static TaskActionFragment newInstance(Favor favor) {
        TaskActionFragment fragment = new TaskActionFragment();
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
        return inflater.inflate(R.layout.fragment_task_action, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Instantiate button
        removeFulfillerBtn = view.findViewById(R.id.remove_fulfiller_Btn);
        if(listener != null) {
            removeFulfillerBtn.setOnClickListener(listener);
        }
    }

    public void setButtonClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }
}