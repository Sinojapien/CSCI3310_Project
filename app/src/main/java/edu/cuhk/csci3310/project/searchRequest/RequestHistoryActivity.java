package edu.cuhk.csci3310.project.searchRequest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.icu.number.CompactNotation;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
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
import edu.cuhk.csci3310.project.model.BorrowingFavor;
import edu.cuhk.csci3310.project.model.DiningFavor;
import edu.cuhk.csci3310.project.model.Favor;
import edu.cuhk.csci3310.project.model.GatheringFavor;
import edu.cuhk.csci3310.project.model.MovingFavor;
import edu.cuhk.csci3310.project.model.TutoringFavor;
import edu.cuhk.csci3310.project.requestDetails.RequestDetailsActivity;

// https://stackoverflow.com/questions/52308648/android-firebase-push-notification-click-event
// android studio send notification upon firebase event
// https://developer.android.com/training/notify-user/build-notification#java
// https://firebase.google.com/docs/database/admin/retrieve-data?hl=en

// Service
// https://developer.android.com/guide/components/foreground-services
// https://stackoverflow.com/questions/50481821/using-a-listener-after-an-android-application-is-closed
// https://stackoverflow.com/questions/29323317/how-to-stop-android-service-when-app-is-closed

public class RequestHistoryActivity extends AppCompatActivity {

    private enum ListType {
        ANY("Any"), ENQUIRE("Enquire"), ACCEPT("Accept");
        private final String value;
        ListType(String value){this.value = value;}
        public static ListType getTypeFrom(String value) {
            switch (value) {
                case "Any":
                    return ListType.ANY;
                case "Enquire":
                    return ListType.ENQUIRE;
                case "Accept":
                    return ListType.ACCEPT;
                default:
                    return null;
            }
        }
        public static String[] getValues() {
            ListType[] listTypes = ListType.values();
            String[] values = new String[listTypes.length];
            for (int i=0; i<listTypes.length; i++){
                values[i] = listTypes[i].value;
            }
            return values;
        }
    }

    private static final String TAG = "RequestHistoryActivity";
    private FirebaseFirestore mFirestore;
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

    private ListType mListType;
    private Status mStatus;
    private boolean mDeleteMode;

    private FirebaseAuth firebaseAuth;

    private class OnHistoryFavorSelectedListener implements FavorAdapter.OnFavorSelectedListener {

        Context mContext;

        //public OnHistoryFavorSelectedListener(Context context){this.mContext = context;}
        public OnHistoryFavorSelectedListener(){}

        @Override
        public void onFavorSelected(DocumentSnapshot favor){
            if (mDeleteMode){

                AlertDialog.Builder builder = new AlertDialog.Builder(RequestHistoryActivity.this);
                builder.setTitle("Delete Favor?");

                builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // https://firebase.google.com/docs/firestore/manage-data/delete-data#java_1
                        mFirestore.collection("favors").document(favor.getId()).delete();
                        //Log.d(TAG, favor.getId());
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

            } else{
                String favorType = favor.getString("taskType");
                Favor newFavor;
                switch(favorType){
                    case "MOVING":
                        newFavor = favor.toObject(MovingFavor.class); break;
                    case "TUTORING":
                        newFavor = favor.toObject(TutoringFavor.class); break;
                    case "DINING":
                        newFavor = favor.toObject(DiningFavor.class); break;
                    case "GATHERING":
                        newFavor = favor.toObject(GatheringFavor.class); break;
                    case "BORROWING":
                        newFavor = favor.toObject(BorrowingFavor.class); break;
                    default:
                        Log.e(TAG, "unknown favor encountered");
                        newFavor = favor.toObject(Favor.class);
                }
                newFavor.setId(favor.getId());
                Intent intent = new Intent(RequestHistoryActivity.this, RequestDetailsActivity.class);
                intent.putExtra("FAVOR", newFavor);
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

        firebaseAuth = FirebaseAuth.getInstance();
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

        // Initialize OnClick Events
        mListTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Spinner listTypeSpinner = new Spinner(view.getContext());
                ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, ListType.getValues());
                listTypeSpinner.setAdapter(adapter);
                listTypeSpinner.setSelection(0);

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("List Filters");
                builder.setView(listTypeSpinner);

                builder.setPositiveButton(R.string.set, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mListType = ListType.getTypeFrom((String)listTypeSpinner.getSelectedItem());
                        filterList(mListType, mStatus);
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
                        mListType = ListType.ANY;
                        mStatus = null;
                        filterList(mListType, null);
                        // also reset buttons
                    }
                });

                builder.show();

            }
        });

        mDeleteMode = false;
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDeleteMode = !mDeleteMode;
                if (mDeleteMode) {
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
                if (mPreviousSelectedButton != null){
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mPreviousSelectedButton.getLayoutParams();
                    layoutParams.gravity = Gravity.BOTTOM;
                    mPreviousSelectedButton.setLayoutParams(layoutParams);

                    String buttonText = mPreviousSelectedButton.getText().toString();
                    mPreviousSelectedButton.setText(buttonText.substring(0, buttonText.length() - 1));

                    mPreviousSelectedButton.setChecked(false);
                    //mPreviousSelectedButton.setClickable(true);

                    if (mPreviousSelectedButton.getId() == view.getId()){
                        mPreviousSelectedButton = null;
                        mStatus = null;
                        filterList(mListType, mStatus);
                        return;
                    }
                }

                ToggleButton buttonView = (ToggleButton) view;
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) buttonView.getLayoutParams();
                layoutParams.gravity = Gravity.NO_GRAVITY;
                buttonView.setLayoutParams(layoutParams);
                buttonView.setText(buttonView.getText().toString() + "\n");
                mPreviousSelectedButton = buttonView;
                //buttonView.setClickable(false);

                switch (view.getId()){
                    case R.id.rh_open_button:
                        mStatus = Status.OPEN;
                        break;
                    case R.id.rh_active_button:
                        mStatus = Status.ACTIVE;
                        break;
                    case R.id.rh_completed_button:
                        mStatus = Status.COMPLETED;
                        break;
                    default:
                        mStatus = null;
                        break;
                }

                filterList(mListType, mStatus);
            }
        };
        mOpenButton.setOnClickListener(buttonOnClickListener);
        mActiveButton.setOnClickListener(buttonOnClickListener);
        mCompletedButton.setOnClickListener(buttonOnClickListener);

    }

    @Override
    public void onStart() {
        Log.d(TAG, "on start");
        super.onStart();

        setFirebaseAdapterQuery(getDefaultFirebaseQuery(ListType.ENQUIRE));

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

    private void filterList(ListType listType, Status status){
        Query query = getDefaultFirebaseQuery(listType);
        if (status != null)
            query = query.whereEqualTo("status", status);
        setFirebaseAdapterQuery(query);
    }

    private void setFirebaseAdapterQuery(Query query){
        Log.d(TAG, "Query: " + query);

        // Construct query
        mQuery = query;
        mAdapter.setQuery(query);

        // Start listening for Firestore updates
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    private Query getDefaultFirebaseQuery(ListType listType){
        // https://stackoverflow.com/questions/71902947/firestore-android-consecutive-wherearraycontains-queries-not-working
        // Logical OR requires >1 queries and combine into 1 ArrayList of objects (Not working with current adapter)
        if (listType == ListType.ACCEPT)
            return mFirestore.collection("favors").limit(50)
                    .whereEqualTo("accepter", firebaseAuth.getCurrentUser().getUid());

        return mFirestore.collection("favors").limit(50)
                .whereEqualTo("enquirer", firebaseAuth.getCurrentUser().getUid());
    }

}