package edu.cuhk.csci3310.project.searchRequest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.cuhk.csci3310.project.R;
import edu.cuhk.csci3310.project.adaptor.FavorAdapter;
import edu.cuhk.csci3310.project.database.Database;
import edu.cuhk.csci3310.project.database.Status;
import edu.cuhk.csci3310.project.model.Favor;
import edu.cuhk.csci3310.project.requestDetails.RequestDetailsActivity;
import edu.cuhk.csci3310.project.viewModel.RequestHistoryViewModel;
import edu.cuhk.csci3310.project.viewModel.RequestHistoryViewModel.ListType;

public class RequestHistoryActivity extends AppCompatActivity {

    private static final String TAG = "RequestHistoryActivity";
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mFirebaseAuth;
    private Query mQuery;

    private RecyclerView mFavorRecycler;
    private FavorAdapter mAdapter;
    private LinearLayout mEmptyView;

    private ToggleButton mOpenButton;
    private ToggleButton mActiveButton;
    private ToggleButton mCompletedButton;
    private ToggleButton mPreviousSelectedButton;
    private FloatingActionButton mListTypeButton;
    private FloatingActionButton mDeleteButton;

    private RequestHistoryViewModel mViewModel;

    private class OnHistoryFavorSelectedListener implements FavorAdapter.OnFavorSelectedListener {
        public OnHistoryFavorSelectedListener(){}

        @Override
        public void onFavorSelected(DocumentSnapshot favor){
            if (mViewModel.mDeleteMode){

                AlertDialog.Builder builder = new AlertDialog.Builder(RequestHistoryActivity.this);
                builder.setTitle("Delete Favor of user " + favor.toObject(Favor.class).getEnquirerName() + " ?");

                builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // https://firebase.google.com/docs/firestore/manage-data/delete-data#java_1
                        mFirestore.collection("favors").document(favor.getId()).delete();
                    }
                });

                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                // Button colour
                // https://stackoverflow.com/questions/27965662/how-can-i-change-default-dialog-button-text-color-in-android-5

                builder.show();

            }else{
                Log.d(TAG, "clicked on favor, ID = " + favor.getId());
                //Intent intent = new Intent(this, favorDetailActivity.class);
                // intent.putExtra(RestaurantDetailActivity.KEY_RESTAURANT_ID, restaurant.getId());
                // startActivity(intent);
                Intent intent = new Intent(RequestHistoryActivity.this, RequestDetailsActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_request_history);

        setTitle("Request History");

        // Initialize all views
        mFavorRecycler = findViewById(R.id.recycler_favor);
        mEmptyView = findViewById(R.id.view_empty);

        mOpenButton = findViewById(R.id.rh_open_button);
        mActiveButton = findViewById(R.id.rh_active_button);
        mCompletedButton = findViewById(R.id.rh_completed_button);
        mListTypeButton = findViewById(R.id.rh_list_type_button);
        mDeleteButton = findViewById(R.id.rh_delete_button);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        // initialize recycler view
        mAdapter = new FavorAdapter(null, new OnHistoryFavorSelectedListener()) {
            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    Log.d(TAG, "itemCount is 0");
                    mFavorRecycler.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);
                } else {
                    Log.d(TAG, "itemCount is not 0. getItemCount() = "+getItemCount());
                    mFavorRecycler.setVisibility(View.VISIBLE);
                    mEmptyView.setVisibility(View.GONE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                Log.e(TAG, "Error occurs, " + e);
            }
        };

        mFavorRecycler.setLayoutManager(new LinearLayoutManager(this));
        mFavorRecycler.setAdapter(mAdapter);

        // Initialize View Model
        if (mViewModel == null)
            mViewModel = ViewModelProviders.of(this).get(RequestHistoryViewModel.class);

        // Initialize OnClick Events
        mListTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // https://www.youtube.com/watch?v=umCX1-Tq25k

                Context context = view.getContext();
                int adapterLayoutID = android.R.layout.simple_spinner_dropdown_item;

                // Inflate Views
                View spinnersLayout = getLayoutInflater().inflate(R.layout.dialog_list_filters, null);

                Spinner listTypeSpinner = spinnersLayout.findViewById(R.id.list_type_spinner);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, adapterLayoutID, ListType.getValues());
                listTypeSpinner.setAdapter(adapter);
                listTypeSpinner.setSelection(adapter.getPosition(mViewModel.mListType.value));

                Spinner sortTypeSpinner = spinnersLayout.findViewById(R.id.sort_type_spinner);
                ArrayAdapter<String> sortTypeAdapter = new ArrayAdapter<>(context, adapterLayoutID, RequestHistoryViewModel.listSortType.clone());
                sortTypeSpinner.setAdapter(sortTypeAdapter);
                sortTypeSpinner.setSelection(mViewModel.mSortType);

                Spinner sortDirectionSpinner = spinnersLayout.findViewById(R.id.sort_direction_spinner);
                ArrayAdapter<String> sortOrderAdapter = new ArrayAdapter<>(context, adapterLayoutID, RequestHistoryViewModel.getQueryDirectionValues());
                sortDirectionSpinner.setAdapter(sortOrderAdapter);
                sortDirectionSpinner.setSelection(sortOrderAdapter.getPosition(mViewModel.mSortDirection.name()));

                // Setup Dialog Builder
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("List Filters");
                builder.setView(spinnersLayout);

                builder.setPositiveButton(R.string.set, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mViewModel.mListType = ListType.getTypeFrom((String)listTypeSpinner.getSelectedItem());
                        mViewModel.mSortType = sortTypeSpinner.getSelectedItemPosition();
                        mViewModel.mSortDirection = Query.Direction.valueOf((String)sortDirectionSpinner.getSelectedItem());
                        setFirebaseAdapterQuery(getFilteredList());
                    }
                });

                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.setNeutralButton(R.string.reset, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mViewModel.reset();
                        setFirebaseAdapterQuery(getFilteredList());
                        // also reset buttons
                    }
                });

                builder.show();
            }
        });

        mViewModel.mDeleteMode = false;
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.mDeleteMode = !mViewModel.mDeleteMode;
                if (mViewModel.mDeleteMode) {
                    mDeleteButton.setSupportBackgroundTintList(ColorStateList.valueOf(Color.RED));
                    mDeleteButton.getDrawable().setTint(getResources().getColor(R.color.white));
                }else {
                    mDeleteButton.setSupportBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.teal_200)));
                    mDeleteButton.getDrawable().setTint(Color.BLACK);
                }
            }
        });

        View.OnClickListener buttonOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String offset = "\n ";

                if (mPreviousSelectedButton != null){
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mPreviousSelectedButton.getLayoutParams();
                    layoutParams.gravity = Gravity.BOTTOM;
                    mPreviousSelectedButton.setLayoutParams(layoutParams);

                    String buttonText = mPreviousSelectedButton.getText().toString();
                    mPreviousSelectedButton.setText(buttonText.substring(0, buttonText.length() - offset.length()));

                    mPreviousSelectedButton.setChecked(false);
                    //mPreviousSelectedButton.setClickable(true);

                    if (mPreviousSelectedButton.getId() == view.getId()){
                        mPreviousSelectedButton = null;
                        mViewModel.mStatus = null;
                        setFirebaseAdapterQuery(getFilteredList());
                        return;
                    }
                }

                ToggleButton buttonView = (ToggleButton) view;
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) buttonView.getLayoutParams();
                layoutParams.gravity = Gravity.NO_GRAVITY;
                buttonView.setLayoutParams(layoutParams);
                buttonView.setText(buttonView.getText().toString() + offset);
                mPreviousSelectedButton = buttonView;
                //buttonView.setClickable(false);

                switch (view.getId()){
                    case R.id.rh_open_button:
                        mViewModel.mStatus = Status.OPEN;
                        break;
                    case R.id.rh_active_button:
                        mViewModel.mStatus = Status.ACTIVE;
                        break;
                    case R.id.rh_completed_button:
                        mViewModel.mStatus = Status.COMPLETED;
                        break;
                    default:
                        mViewModel.mStatus = null;
                        break;
                }

                setFirebaseAdapterQuery(getFilteredList());
            }
        };

        mOpenButton.setOnClickListener(buttonOnClickListener);
        mActiveButton.setOnClickListener(buttonOnClickListener);
        mCompletedButton.setOnClickListener(buttonOnClickListener);

        if (mViewModel.mStatus == Status.OPEN)
            mOpenButton.callOnClick();
        else if (mViewModel.mStatus == Status.ACTIVE)
            mActiveButton.callOnClick();
        else if (mViewModel.mStatus == Status.COMPLETED)
            mCompletedButton.callOnClick();

    }

    @Override
    public void onStart() {
        super.onStart();

        setFirebaseAdapterQuery(getFilteredList());

        // https://stackoverflow.com/questions/50035752/how-to-get-list-of-documents-from-a-collection-in-firestore-android
        // https://firebase.google.com/docs/firestore/query-data/get-data#java
        // https://firebase.google.com/docs/firestore/query-data/listen
        // https://stackoverflow.com/questions/51000169/how-to-check-a-certain-data-already-exists-in-firestore-or-not

//        mFirestore.collection("favors").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    List<String> list = new ArrayList<>();
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        list.add(document.getId());
//                    }
//                    Log.d(TAG, list.toString());
//                } else {
//                    Log.d(TAG, "Error getting documents: ", task.getException());
//                }
//            }
//        });

    }

    private void setFirebaseAdapterQuery(Query query){
        // Construct query
        mQuery = query;
        mAdapter.setQuery(query);

        // Start listening for Firestore updates
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    private Query getFilteredList(){
        return mViewModel.filterQuery(mFirestore.collection("favors").limit(50), mFirebaseAuth.getCurrentUser().getUid());
    }

    private void setFilteredList(ListType listType, Status status, int sortType, Query.Direction direction){
        Query query = getDefaultFirebaseQuery(listType);
        if (status != null)
            query = query.whereEqualTo("status", status);
        if (sortType > 0)
            query = query.orderBy(RequestHistoryViewModel.getFavorMemberName(sortType), direction);
        setFirebaseAdapterQuery(query);
    }

    private Query getDefaultFirebaseQuery(ListType listType){
        // https://stackoverflow.com/questions/71902947/firestore-android-consecutive-wherearraycontains-queries-not-working
        // Logical OR requires >1 queries and combine into 1 ArrayList of objects (Not working with current adapter)
        if (listType == ListType.ACCEPT)
            return mFirestore.collection("favors").limit(50)
                    .whereEqualTo("accepter", mFirebaseAuth.getCurrentUser().getUid());

        return mFirestore.collection("favors").limit(50)
                .whereEqualTo("enquirer", mFirebaseAuth.getCurrentUser().getUid());
    }

}