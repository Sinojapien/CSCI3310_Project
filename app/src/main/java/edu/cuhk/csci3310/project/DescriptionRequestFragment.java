package edu.cuhk.csci3310.project;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

public class DescriptionRequestFragment extends Fragment {

    public TextView mTitleText;
    public TextView mWordCountText;
    public TextView mDescriptionEdit;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public DescriptionRequestFragment() {
        // Required empty public constructor
    }

    public static DescriptionRequestFragment newInstance() {
        DescriptionRequestFragment fragment = new DescriptionRequestFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_description, container, false);
        mTitleText = view.findViewById(R.id.description_title);
        mWordCountText = view.findViewById(R.id.wordcount_text);
        mDescriptionEdit = view.findViewById(R.id.description_edit);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int descriptionMaxLength = getResources().getInteger(R.integer.request_description_max_length);
        mWordCountText.setText(getString(R.string.request_description_wordcount, 0, descriptionMaxLength));

        mDescriptionEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // int testLength = charSequence.toString().length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mWordCountText.setText(getString(R.string.request_description_wordcount, charSequence.toString().length(), descriptionMaxLength));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public boolean isFilled(){
        return mDescriptionEdit.getText().toString().length() > 0;
    }

    public String getInformationString(){
        return mDescriptionEdit.getText().toString();
    }

}