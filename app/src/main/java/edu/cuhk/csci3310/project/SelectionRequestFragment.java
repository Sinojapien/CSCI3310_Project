package edu.cuhk.csci3310.project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;

import edu.cuhk.csci3310.project.createRequest.RequestFragment;

public class SelectionRequestFragment extends RequestFragment {

    // Views
    public TextView mTitleText;
    public RecyclerView mRecyclerView;
    public SelectionListAdapter mAdapter;

    // Variables
    private String mParamTitle;
    private String mParamListTitle;
    private ArrayList<String> mParamList;
    private ArrayList<String> mDefaultSelectedList;

    private static final String ARG_PARAM_TITLE = "param1";
    private static final String ARG_PARAM_LIST_TITLE = "param2";
    private static final String ARG_PARAM_LIST = "param3";
    private static final String INSTANCE_DEFAULT_LIST = "param4";

    public SelectionRequestFragment() {
        // Required empty public constructor
    }

    public static SelectionRequestFragment newInstance(String title, String listTitle, ArrayList<String> itemList) {
        SelectionRequestFragment fragment = new SelectionRequestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_TITLE, title);
        args.putString(ARG_PARAM_LIST_TITLE, listTitle);
        args.putStringArrayList(ARG_PARAM_LIST, itemList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamTitle = getArguments().getString(ARG_PARAM_TITLE);
            mParamListTitle = getArguments().getString(ARG_PARAM_LIST_TITLE);
            mParamList = getArguments().getStringArrayList(ARG_PARAM_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_selection, container, false);
        mTitleText = view.findViewById(R.id.selection_title);
        mRecyclerView = view.findViewById(R.id.selection_recycler);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mParamTitle != null)
            mTitleText.setText(mParamTitle);

        // SelectionListAdapter adapter = new SelectionListAdapter(view.getContext(), mParamList, 0);
        mAdapter = new SelectionListAdapter(this, mDefaultSelectedList, 0);
        mRecyclerView.setAdapter(mAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 3);
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        //mRecyclerView.setHasFixedSize(true);

        if (mParamList != null)
            setMultiChoiceEdit(mTitleText, mAdapter);
        else
            setCustomChoiceEdit(mTitleText, mAdapter);
    }

    protected void setMultiChoiceEdit(View view, SelectionListAdapter adapter){
        // https://www.geeksforgeeks.org/alert-dialog-with-multipleitemselection-in-android/
        // https://www.geeksforgeeks.org/alert-dialog-with-singleitemselection-in-android/
        // https://developer.android.com/reference/kotlin/androidx/appcompat/app/AlertDialog.Builder
        // https://developer.android.com/guide/topics/ui/dialogs#java

        view.setOnClickListener(new View.OnClickListener() {

            //final boolean[] checkedIndex = new boolean[mParamList.size()];

            @Override
            public void onClick(View view) {

                boolean[] checkedIndex = new boolean[mParamList.size()];
                // Initialize index array, inefficient
                ArrayList<String> selectedList = mAdapter.getItemList();
                for(int i=0; i<mParamList.size(); i++){
                    if (selectedList.contains(mParamList.get(i)))
                        checkedIndex[i] = true;
                    else
                        checkedIndex[i] = false;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle(mParamListTitle);
                builder.setIcon(R.drawable.common_google_signin_btn_icon_light);
                builder.setMultiChoiceItems(mParamList.toArray(new String[0]), checkedIndex, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                        checkedIndex[i] = isChecked;
                    }
                });

                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ArrayList<String> checkedItems = new ArrayList<>();
                        for(int p=0; p<checkedIndex.length;p++){
                            if (checkedIndex[p])
                                checkedItems.add(mParamList.get(p));
                        }
                        adapter.clearItem();
                        adapter.addItem(checkedItems);
                    }
                });

                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.setNeutralButton(R.string.clear, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Arrays.fill(checkedIndex, false);
                        adapter.clearItem();
                    }
                });

                builder.show();
            }
        });
    }

    protected void setCustomChoiceEdit(View view, SelectionListAdapter adapter){
        // https://stackoverflow.com/questions/18799216/how-to-make-a-edittext-box-in-a-dialog

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText editText = new EditText(view.getContext());
                editText.setHint("Enter item");
                editText.setMaxLines(1);

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle(mParamListTitle);
                builder.setIcon(R.drawable.common_google_signin_btn_icon_light);
                builder.setView(editText);

                builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String item = editText.getText().toString();
                        if(item.length() > 0)
                            if (!adapter.hasItem(item))
                                adapter.addItem(item);
                    }
                });

                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.setNeutralButton(R.string.clear, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.clearItem();
                    }
                });

                builder.show();
            }
        });
    }

    @Override
    public void onSaveInstanceState(@Nullable Bundle savedInstanceState){
        savedInstanceState.putString(ARG_PARAM_TITLE, mTitleText.getText().toString());
        savedInstanceState.putString(ARG_PARAM_LIST_TITLE, mParamListTitle);
        savedInstanceState.putStringArrayList(ARG_PARAM_LIST, mParamList);
        savedInstanceState.putStringArrayList(INSTANCE_DEFAULT_LIST, mAdapter.getItemList());
    }

    @Override
    protected void onLoadInstanceState(@Nullable Bundle savedInstanceState){
        mParamTitle = savedInstanceState.getString(ARG_PARAM_TITLE);
        mParamListTitle = savedInstanceState.getString(ARG_PARAM_LIST_TITLE);
        mParamList = savedInstanceState.getStringArrayList(ARG_PARAM_LIST);
        mDefaultSelectedList = savedInstanceState.getStringArrayList(INSTANCE_DEFAULT_LIST);
    }

    @Override
    public boolean isFilled(){
        return mAdapter.getItemCount() > 0;
    }

    public ArrayList<String> getInformationStringList(){
        return mAdapter.getItemList();
    }

}