package edu.cuhk.csci3310.project.createRequest;

// Name: Yeung Chi Ho, SID: 1155126460

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Debug;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.cuhk.csci3310.project.R;

public class DescriptionRequestFragment extends RequestFragment {

    public TextView mTitleText;
    public TextView mWordCountText;
    public TextView mDescriptionEdit;

    private static final String ARG_PARAM_TITLE = "param1";

//    private static final String INSTANCE_TITLE = "TITLE";
//    private static final String INSTANCE_DESCRIPTION = "DESCRIPTION";

    private String mParamTitle;

    public DescriptionRequestFragment() {
        // Required empty public constructor
    }

    public static DescriptionRequestFragment newInstance(String param1) {
        DescriptionRequestFragment fragment = new DescriptionRequestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_TITLE, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamTitle = getArguments().getString(ARG_PARAM_TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_description, container, false);
        mTitleText = view.findViewById(R.id.description_title);
        mWordCountText = view.findViewById(R.id.wordcount_text);
        mDescriptionEdit = view.findViewById(R.id.description_edit);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTitleText.setText(mParamTitle);

        int descriptionMaxLength = getResources().getInteger(R.integer.request_description_max_length);
        mWordCountText.setText(getString(R.string.request_description_wordcount, mDescriptionEdit.getText().toString().length(), descriptionMaxLength));

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

//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putString(INSTANCE_TITLE, mTitleText.getText().toString());
//        outState.putString(INSTANCE_DESCRIPTION, mDescriptionEdit.getText().toString());
//    }
//
//    @Override
//    protected void loadInstanceState(@Nullable Bundle savedInstanceState){
//        Log.w("TEST", "" + savedInstanceState);
//        mTitleText.setText(savedInstanceState.getString(INSTANCE_TITLE, ""));
//        mDescriptionEdit.setText(savedInstanceState.getString(INSTANCE_DESCRIPTION, ""));
//    }

    @Override
    public boolean isFilled(){
        return mDescriptionEdit.getText().toString().length() > 0;
    }

    public String getInformationString(){
        return mDescriptionEdit.getText().toString();
    }

}