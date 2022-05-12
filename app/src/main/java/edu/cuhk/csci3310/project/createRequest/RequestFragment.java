package edu.cuhk.csci3310.project.createRequest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.cuhk.csci3310.project.R;

public class RequestFragment extends Fragment {

//    protected SharedPreferences mPreferences;
//    protected final String sharedPreferenceFile = "edu.cuhk.csci3310.project.request.fragment";

//    protected void getPreference(String identifier){
//        if (mPreferences == null)
//            mPreferences = getActivity().getSharedPreferences(sharedPreferenceFile + "." + identifier, Context.MODE_PRIVATE);
//    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null)
            onLoadInstanceState(savedInstanceState);
    }

//    protected void loadPreferences(){}

//    protected void savePreferences(){}

    protected void onLoadInstanceState(@Nullable Bundle savedInstanceState){}

    public boolean isFilled(){
        return true;
    }

}
