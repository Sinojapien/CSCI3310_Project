package edu.cuhk.csci3310.project.createRequest;

import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import edu.cuhk.csci3310.project.R;

public class TimeRequestFragment extends Fragment {

    public TextView mTimeTitle;
    public TextView mTimeEdit;
    public TextView mEndTimeEdit;

    private static final String ARG_PARAM_DURATION = "param1";

    private Boolean mParamDuration;

    public TimeRequestFragment() {
        // Required empty public constructor
    }

    public static TimeRequestFragment newInstance(Boolean param1) {
        TimeRequestFragment fragment = new TimeRequestFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM_DURATION, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamDuration = getArguments().getBoolean(ARG_PARAM_DURATION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        if (mParamDuration){
            view = inflater.inflate(R.layout.fragment_request_time_duration, container, false);
            mTimeTitle = view.findViewById(R.id.date_title);
            mTimeEdit = view.findViewById(R.id.start_date_edit);
            mEndTimeEdit = view.findViewById(R.id.end_date_edit);
        }else {
            view = inflater.inflate(R.layout.fragment_request_time, container, false);
            mTimeTitle = view.findViewById(R.id.date_title);
            mTimeEdit = view.findViewById(R.id.time_edit);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setTimePickerEditText(mTimeEdit);

        if (mParamDuration){
            setTimePickerEditText(mEndTimeEdit);
        }

    }

    protected void setTimePickerEditText(TextView textView){
        // https://www.journaldev.com/9976/android-date-time-picker-dialog
        // https://stackoverflow.com/questions/41829665/android-studioedittext-editable-is-deprecated-how-to-use-inputtype
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        textView.setCursorVisible(false);
        textView.setFocusable(false);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        String timeString = getString(R.string.request_time, i, i1);
                        textView.setText(timeString);
                    }
                }, hour, minute, true);

                timePickerDialog.show();

            }
        });
    }

    public boolean isFilled(){
        if (mParamDuration)
            return mTimeEdit.getText().toString().length() > 0 && mEndTimeEdit.getText().toString().length() > 0;

        return mTimeEdit.getText().toString().length() > 0;
    }

    public String getInformationTime(){
        return mTimeEdit.getText().toString();
    }

    public String getInformationTimeEnd(){
        if (mParamDuration)
            return mEndTimeEdit.getText().toString();
        return getInformationTime();
    }

}