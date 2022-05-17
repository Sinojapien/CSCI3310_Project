package edu.cuhk.csci3310.project.createRequest;

// Name: Yeung Chi Ho, SID: 1155126460

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.util.Calendar;
import java.util.Date;

import edu.cuhk.csci3310.project.R;

public class DateRequestFragment extends RequestFragment {

    public TextView mDateTitle;
    public EditText mDateEdit;
    public EditText mEndDateEdit;
    public Switch mDurationSwitch;

    private static final String ARG_PARAM_TITLE = "param1";
    private static final String ARG_PARAM_DURATION = "param2";

    private String mParamTitle;
    private Boolean mParamDuration;

    public DateRequestFragment() {
        // Required empty public constructor
    }

    public static DateRequestFragment newInstance(String title, Boolean hasDuration) {
        DateRequestFragment fragment = new DateRequestFragment();
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
        if (mParamDuration){
            view = inflater.inflate(R.layout.fragment_request_date_duration, container, false);
            mDateTitle = view.findViewById(R.id.date_title);
            mDateEdit = view.findViewById(R.id.start_date_edit);
            mEndDateEdit = view.findViewById(R.id.end_date_edit);
            mDurationSwitch = view.findViewById(R.id.date_switch);
        }else{
            view = inflater.inflate(R.layout.fragment_request_date, container, false);
            mDateTitle = view.findViewById(R.id.date_title);
            mDateEdit = view.findViewById(R.id.date_edit);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mParamTitle != null)
            mDateTitle.setText(mParamTitle);

        if (mParamDuration){
            mDurationSwitch.setVisibility(View.INVISIBLE);
            setMaterialDatePickerEdit(mDateEdit, mEndDateEdit);
        }else{
            setDatePickerEdit(mDateEdit);
        }
    }

    protected void setDatePickerEdit(TextView textView) {
        // https://www.techypid.com/datepicker-dialog-click-on-edittext-in-android/
        // https://xken831.pixnet.net/blog/post/460794230-%5Bandroid%5D-%E6%8C%91%E9%81%B8%E6%99%82%E9%96%93%E5%85%83%E4%BB%B6-datepickerdialog-%E4%BD%BF%E7%94%A8%E6%96%B9%E5%BC%8F
        // https://www.geeksforgeeks.org/material-design-date-picker-in-android/
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                textView.setText(getString(R.string.request_date, i, i1+1, i2));
            }
        }, year, month, day);
        // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        calendar.set(year, month + getResources().getInteger(R.integer.request_max_duration), day);
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

        textView.setCursorVisible(false);
        textView.setFocusable(false);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });
    }

    protected void setMaterialDatePickerEdit(TextView startTextView, TextView endTextView) {
        // https://www.techypid.com/datepicker-dialog-click-on-edittext-in-android/
        // https://xken831.pixnet.net/blog/post/460794230-%5Bandroid%5D-%E6%8C%91%E9%81%B8%E6%99%82%E9%96%93%E5%85%83%E4%BB%B6-datepickerdialog-%E4%BD%BF%E7%94%A8%E6%96%B9%E5%BC%8F
        // https://www.geeksforgeeks.org/material-design-date-picker-in-android/
        Calendar startCalendar = Calendar.getInstance();
        int year = startCalendar.get(Calendar.YEAR);
        int month = startCalendar.get(Calendar.MONTH);
        int day = startCalendar.get(Calendar.DAY_OF_MONTH);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(year, month + getResources().getInteger(R.integer.request_max_duration), day);

        MaterialDatePicker.Builder<Pair<Long, Long>> materialDateBuilder = MaterialDatePicker.Builder.dateRangePicker();
        materialDateBuilder.setTitleText("SELECT DATES");
        materialDateBuilder.setCalendarConstraints(new CalendarConstraints.Builder().setOpenAt(
                startCalendar.getTimeInMillis()
        ).setStart(
                startCalendar.getTimeInMillis()
        ).setEnd(
                endCalendar.getTimeInMillis()
        ).build());

        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                Pair<Long, Long> datePair = (Pair<Long, Long>) selection;
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(datePair.first);
                startTextView.setText(getString(R.string.request_date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH) ));
                calendar.setTimeInMillis(datePair.second);
                endTextView.setText(getString(R.string.request_date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DAY_OF_MONTH)));
            }
        });

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
            }
        };

        startTextView.setCursorVisible(false);
        startTextView.setFocusable(false);
        endTextView.setCursorVisible(false);
        endTextView.setFocusable(false);
        startTextView.setOnClickListener(clickListener);
        endTextView.setOnClickListener(clickListener);
    }

    @Override
    public boolean isFilled(){
        if (mParamDuration)
            return mDateEdit.getText().toString().length() > 0 && mEndDateEdit.getText().toString().length() > 0;

        return mDateEdit.getText().toString().length() > 0;
    }

    public String getInformationDate(){
        return mDateEdit.getText().toString();
    }

    public String getInformationDateEnd(){
        if (mParamDuration)
            return mEndDateEdit.getText().toString();
        return getInformationDate();
    }

}