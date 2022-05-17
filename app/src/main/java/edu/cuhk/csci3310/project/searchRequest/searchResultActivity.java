package edu.cuhk.csci3310.project.searchRequest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.core.OrderBy;

import edu.cuhk.csci3310.project.R;
import edu.cuhk.csci3310.project.adaptor.FavorAdapter;
import edu.cuhk.csci3310.project.model.BorrowingFavor;
import edu.cuhk.csci3310.project.model.DiningFavor;
import edu.cuhk.csci3310.project.model.Favor;
import edu.cuhk.csci3310.project.model.GatheringFavor;
import edu.cuhk.csci3310.project.model.MovingFavor;
import edu.cuhk.csci3310.project.model.TutoringFavor;
import edu.cuhk.csci3310.project.requestDetails.RequestDetailsActivity;

public class searchResultActivity extends AppCompatActivity implements
        FavorAdapter.OnFavorSelectedListener{

    private static final String TAG = "SearchResultActivity";
    private FirebaseFirestore mFirestore;
    private Query mQuery;

    private RecyclerView mFavorRecycler;
    private FavorAdapter mAdapter;
    private LinearLayout mEmptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Log.d(TAG, "on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        // initFirestore();
        mFirestore = FirebaseFirestore.getInstance();

        mFavorRecycler = findViewById(R.id.recycler_favor);
        mEmptyView = findViewById(R.id.view_empty);

        // receive the favor object, so that the activity could decide the query
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String favorType = extras.getString("favorType");
        // Log.d(TAG, "get favorType: " + favorType);
        // construct query based on the favor
        mQuery = mFirestore.collection("favors")
                .whereEqualTo("status", "OPEN")
                .limit(50);

        // name search
        String enquirerName = extras.getString("enquirerName");
        if(!enquirerName.isEmpty())
            mQuery = mQuery.whereEqualTo("enquirerName", enquirerName);
        // firebase only allow range search in single field, used in date
        String startDate = extras.getString("startDate");
        String endDate = extras.getString("startDate");


        // get different information basic on favorType
        switch(favorType){
            case "all": // no need to modify query with all
                String sortOrder = extras.getString("sortOrder");
                if(sortOrder.equals("Asc"))
                    mQuery = mQuery.orderBy("taskType", Query.Direction.ASCENDING);
                else
                    mQuery = mQuery.orderBy("taskType", Query.Direction.DESCENDING);
                break;
            case "Borrowing":
                mQuery = mQuery.whereEqualTo("taskType", "BORROWING");
                if(!startDate.isEmpty())
                    mQuery = mQuery.whereGreaterThanOrEqualTo("startDate", startDate);
                if(!endDate.isEmpty())
                    mQuery = mQuery.whereLessThanOrEqualTo("endDate", endDate);

                String itemType = extras.getString("itemType");
                if(!itemType.equalsIgnoreCase("any"))
                    mQuery = mQuery.whereEqualTo("itemType", itemType);
                // Log.d(TAG, "get itemType: " + intent.getStringExtra("itemType"));
                String itemName = extras.getString("itemName");
                if(!itemName.isEmpty())
                    mQuery = mQuery.whereArrayContains("selection", itemName);
                break;
            case "Dining":
                mQuery = mQuery.whereEqualTo("taskType", "DINING");
                if(!startDate.isEmpty())
                    mQuery = mQuery.whereGreaterThanOrEqualTo("date", startDate);
                if(!endDate.isEmpty())
                    mQuery = mQuery.whereLessThanOrEqualTo("date", endDate);
                break;
            case "Gathering":
                mQuery = mQuery.whereEqualTo("taskType", "GATHERING");
                if(!startDate.isEmpty())
                    mQuery = mQuery.whereGreaterThanOrEqualTo("date", startDate);
                if(!endDate.isEmpty())
                    mQuery = mQuery.whereLessThanOrEqualTo("date", endDate);
                break;
            case "Moving":
                mQuery = mQuery.whereEqualTo("taskType", "MOVING");
                if(!startDate.isEmpty())
                    mQuery = mQuery.whereGreaterThanOrEqualTo("date", startDate);
                if(!endDate.isEmpty())
                    mQuery = mQuery.whereLessThanOrEqualTo("date", endDate);
                break;
            case "Tutoring":
                mQuery = mQuery.whereEqualTo("taskType", "TUTORING");
                if(!startDate.isEmpty())
                    mQuery = mQuery.whereGreaterThanOrEqualTo("startDate", startDate);
                if(!endDate.isEmpty())
                    mQuery = mQuery.whereLessThanOrEqualTo("endDate", endDate);

                String courseCode = extras.getString("courseCode");
                if(!courseCode.isEmpty())
                    mQuery = mQuery.whereEqualTo("courseCode", courseCode);
                // Log.d(TAG, "get itemType: " + intent.getStringExtra("itemType"));
                String tutoringType = extras.getString("tutoringType");
                if(!tutoringType.equalsIgnoreCase("any"))
                    mQuery = mQuery.whereArrayContains("selection", tutoringType);

                Log.d(TAG, "courseCode : " + courseCode);
                Log.d(TAG, "tutoringType : " + tutoringType);
                break;
        }

        mAdapter = new FavorAdapter(mQuery, this) {
            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    //Log.d(TAG, "itemCount is 0");
                    mFavorRecycler.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);
                } else {
                    //Log.d(TAG, "itemCount is not 0. getItemCount() = "+getItemCount());
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
    }

    @Override
    public void onStart() {
        //Log.d(TAG, "on start");
        super.onStart();

        // Apply filters
        // onFilter(mViewModel.getFilters());

        // Construct query
        mAdapter.setQuery(mQuery);

        // Start listening for Firestore updates
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    // method to be called when favor is clicked
    // method passed to the adaptor
    @Override
    public void onFavorSelected(DocumentSnapshot favorDoc) {
        // Log.d(TAG, "clicked on favor, ID = " + favorDoc.getId());
        Intent intent = new Intent(searchResultActivity.this, RequestDetailsActivity.class);

        Favor temFavor;
        String favorType = favorDoc.getString("taskType");
        switch(favorType){
            case "MOVING":
                temFavor = favorDoc.toObject(MovingFavor.class); break;
            case "TUTORING":
                temFavor = favorDoc.toObject(TutoringFavor.class); break;
            case "DINING":
                temFavor = favorDoc.toObject(DiningFavor.class); break;
            case "GATHERING":
                temFavor = favorDoc.toObject(GatheringFavor.class); break;
            case "BORROWING":
                temFavor = favorDoc.toObject(BorrowingFavor.class); break;
            default:
                Log.e(TAG, "unknown favor encountered");
                temFavor = favorDoc.toObject(Favor.class);
        }

        intent.putExtra("favor", temFavor);
        startActivity(intent);
    }
}