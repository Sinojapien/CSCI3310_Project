package edu.cuhk.csci3310.project.myRequests;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

import edu.cuhk.csci3310.project.R;
import edu.cuhk.csci3310.project.database.Database;
import edu.cuhk.csci3310.project.database.TaskType;
import edu.cuhk.csci3310.project.model.Favor;

public class RequestOverviewActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;

    public static final String TAG = "MyRequestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_requests);

        // database
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // query favors
        db.collection("favors")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null) {
                            Log.w(TAG, "Listen failed", error);
                        }
                        ArrayList<Favor> favors = new ArrayList<>();
                        for(QueryDocumentSnapshot doc : value) {
                            Map<String, Object> data = doc.getData();
                            try {
                                Favor favor = Database.generateFavorFromDB(doc.getId(), data);
                                favors.add(favor);
                            } catch(Exception e) {
                                Log.d(TAG, "Error: " + e.getMessage());
                            }
                        }
                    }
                });
    }
}