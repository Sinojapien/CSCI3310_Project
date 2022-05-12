package edu.cuhk.csci3310.project.createRequest;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import edu.cuhk.csci3310.project.R;

public class TimeRequestFragment extends RequestFragment {

    public TextView mTimeTitle;
    public EditText mTimeEdit;
    public EditText mEndTimeEdit;
    public Switch mDurationSwitch;
    private TextView mToText;
    private TextView mFromText;

    private static final String ARG_PARAM_TITLE = "param1";
    private static final String ARG_PARAM_DURATION = "param2";

    private String mParamTitle;
    private Boolean mParamDuration;

    public TimeRequestFragment() {
        // Required empty public constructor
    }

    public static TimeRequestFragment newInstance(String title, Boolean hasDuration) {
        TimeRequestFragment fragment = new TimeRequestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_TITLE, title);
        args.putBoolean(ARG_PARAM_DURATION, hasDuration);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamTitle = getArguments().getString(ARG_PARAM_TITLE);
            mParamDuration = getArguments().getBoolean(ARG_PARAM_DURATION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_request_time_duration, container, false);
        mTimeTitle = view.findViewById(R.id.time_title);
        mTimeEdit = view.findViewById(R.id.start_time_edit);
        mEndTimeEdit = view.findViewById(R.id.end_time_edit);
        mDurationSwitch = view.findViewById(R.id.time_switch);
        mToText = view.findViewById(R.id.time_to_text);
        mFromText = view.findViewById(R.id.time_from_text);
//        if (mParamDuration){
//            view = inflater.inflate(R.layout.fragment_request_time_duration, container, false);
//            mTimeTitle = view.findViewById(R.id.time_title);
//            mTimeEdit = view.findViewById(R.id.start_time_edit);
//            mEndTimeEdit = view.findViewById(R.id.end_time_edit);
//            mDurationSwitch = view.findViewById(R.id.time_switch);
//        }else {
//            view = inflater.inflate(R.layout.fragment_request_time, container, false);
//            mTimeTitle = view.findViewById(R.id.time_title);
//            mTimeEdit = view.findViewById(R.id.time_edit);
//        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mParamTitle != null)
            mTimeTitle.setText(mParamTitle);

        setTimePickerEditText(mTimeEdit);
        setTimePickerEditText(mEndTimeEdit);

//        if (mParamDuration){
//            setTimePickerEditText(mEndTimeEdit);
//        }

        setDurationSwitch(mParamDuration);

        mDurationSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDurationSwitch(!mParamDuration);
            }
        });

    }

    protected void setDurationSwitch(boolean checked){
        mParamDuration = checked;
        mDurationSwitch.setChecked(checked);
        mTimeEdit.setTextColor(Color.BLACK);
        mEndTimeEdit.setTextColor(Color.BLACK);
        if (checked){
            mToText.setVisibility(View.VISIBLE);
            mFromText.setVisibility(View.VISIBLE);
            mEndTimeEdit.setVisibility(View.VISIBLE);
        }else{
            mToText.setVisibility(View.INVISIBLE);
            mFromText.setVisibility(View.INVISIBLE);
            mEndTimeEdit.setVisibility(View.INVISIBLE);
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

        final TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                textView.setText(getString(R.string.request_time, i, i1));
                textView.setTextColor(Color.BLACK);
            }
        }, hour, minute, true);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog.show();
            }
        });
    }

    @Override
    public boolean isFilled(){
        if (mParamDuration) {
            String startTime = mTimeEdit.getText().toString();
            String endTime = mEndTimeEdit.getText().toString();

            if (startTime.length() > 0 && endTime.length() > 0){
                if (startTime.compareTo(endTime) <= 0)
                    return true;
                // https://stackoverflow.com/questions/52333750/problem-with-display-multiple-toast-in-order-one-after-another
                // Invalid Input
                Toast.makeText(getContext(), mTimeTitle.getText().toString() + " " + "Please fill in a valid interval.", Toast.LENGTH_SHORT).show();
                mTimeEdit.setTextColor(Color.RED);
                mEndTimeEdit.setTextColor(Color.RED);
            }

            return false;
            //return mTimeEdit.getText().toString().length() > 0 && mEndTimeEdit.getText().toString().length() > 0;
        }

        return mTimeEdit.getText().toString().length() > 0;
    }

    public String getInformationTime(){
        return mTimeEdit.getText().toString();
    }

    public String getInformationTimeEnd(){
        if (mParamDuration)
            return mEndTimeEdit.getText().toString();

        //return getInformationTime();
        return null;
    }

}