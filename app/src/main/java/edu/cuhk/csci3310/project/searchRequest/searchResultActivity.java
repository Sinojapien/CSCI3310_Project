package edu.cuhk.csci3310.project.searchRequest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import edu.cuhk.csci3310.project.R;
import edu.cuhk.csci3310.project.adaptor.FavorAdapter;
import edu.cuhk.csci3310.project.model.Favor;
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
        Favor favor = (Favor) intent.getParcelableExtra("favor");
        // Log.d(TAG, "received favor, status = " + favor.getStatusString());

        mQuery = mFirestore.collection("favors")
                .limit(50);

        // initialize recycler view
        if (mQuery == null) {
            Log.w(TAG, "No query, not initializing RecyclerView");
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
        Query query = mFirestore.collection("favors").limit(50);
        mQuery = query;
        mAdapter.setQuery(query);

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
    public void onFavorSelected(DocumentSnapshot favor) {
        Log.d(TAG, "clicked on favor, ID = " + favor.getId());
        // Go to the details page for the selected favor
        // to be added when favor detail page created
        //Intent intent = new Intent(this, favorDetailActivity.class);
        // intent.putExtra(RestaurantDetailActivity.KEY_RESTAURANT_ID, restaurant.getId());
        // startActivity(intent);
        Intent intent = new Intent(searchResultActivity.this, RequestDetailsActivity.class);
        startActivity(intent);
    }
}