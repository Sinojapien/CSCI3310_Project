package edu.cuhk.csci3310.project;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class DateRequestFragment extends Fragment {

    public TextView mDateTitle;
    public TextView mDateEdit;
    public TextView mEndDateEdit;

    private static final String ARG_PARAM_DURATION = "param1";

    private boolean mParamDuration;

    public DateRequestFragment() {
        // Required empty public constructor
    }

    public static DateRequestFragment newInstance(Boolean param1) {
        DateRequestFragment fragment = new DateRequestFragment();
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
            // Wait to implement
            view = inflater.inflate(R.layout.fragment_request_date, container, false);
            mDateTitle = view.findViewById(R.id.time_title);
            mDateEdit = view.findViewById(R.id.date_edit);
            mEndDateEdit = mDateEdit;
        }else{
            view = inflater.inflate(R.layout.fragment_request_date, container, false);
            mDateTitle = view.findViewById(R.id.time_title);
            mDateEdit = view.findViewById(R.id.date_edit);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setDatePickerEditText(mDateEdit);

        if (mParamDuration){
            setDatePickerEditText(mEndDateEdit);
        }

    }

    protected void setDatePickerEditText(TextView textView) {
        // https://www.techypid.com/datepicker-dialog-click-on-edittext-in-android/
        // https://xken831.pixnet.net/blog/post/460794230-%5Bandroid%5D-%E6%8C%91%E9%81%B8%E6%99%82%E9%96%93%E5%85%83%E4%BB%B6-datepickerdialog-%E4%BD%BF%E7%94%A8%E6%96%B9%E5%BC%8F
        Calendar calendar = Calendar.getInstance();
        long currentTime = calendar.getTimeInMillis();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        textView.setCursorVisible(false);
        textView.setFocusable(false);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        textView.setText(getString(R.string.request_date, i2, i1, i));
                    }
                }, year, month, day);

                // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.getDatePicker().setMinDate(currentTime);
                datePickerDialog.show();
            }
        });
    }

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