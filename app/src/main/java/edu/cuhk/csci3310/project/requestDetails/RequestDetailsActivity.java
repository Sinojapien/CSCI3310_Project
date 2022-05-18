package edu.cuhk.csci3310.project.requestDetails;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import edu.cuhk.csci3310.project.R;
import edu.cuhk.csci3310.project.createRequest.RequestActivity;
import edu.cuhk.csci3310.project.database.Database;
import edu.cuhk.csci3310.project.database.Status;
import edu.cuhk.csci3310.project.model.BorrowingFavor;
import edu.cuhk.csci3310.project.model.DiningFavor;
import edu.cuhk.csci3310.project.model.Favor;
import edu.cuhk.csci3310.project.model.GatheringFavor;
import edu.cuhk.csci3310.project.model.MovingFavor;
import edu.cuhk.csci3310.project.model.TutoringFavor;
import edu.cuhk.csci3310.project.requestDetails.actionFragments.FavorActionFragment;
import edu.cuhk.csci3310.project.requestDetails.actionFragments.RequestActionFragment;
import edu.cuhk.csci3310.project.requestDetails.actionFragments.TaskActionFragment;
import edu.cuhk.csci3310.project.requestDetails.favorFragments.BorrowingFragment;
import edu.cuhk.csci3310.project.requestDetails.favorFragments.DiningFragment;
import edu.cuhk.csci3310.project.requestDetails.favorFragments.GatheringFragment;
import edu.cuhk.csci3310.project.requestDetails.favorFragments.MovingFragment;
import edu.cuhk.csci3310.project.requestDetails.favorFragments.TutoringFragment;


public class RequestDetailsActivity extends FragmentActivity {

    private static final String TAG = "RequestDetailsActivity";

    private Favor favor;

    FirebaseFirestore db;
    FirebaseUser user;

    // View objects
    private TextView requestTypeTV;
    private TextView statusTV;
    private TextView enquirerTV;
    private TextView accepterTV;

    // Listener objects
    private View.OnClickListener markCompletedListener;
    private View.OnClickListener removeFulfillerListener;
    private View.OnClickListener assignFavorListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_request_details);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = getIntent();
        favor = intent.getParcelableExtra("FAVOR");
        instantiateTextFields();
        fillTextFields();
        createListeners();
        showFavorFragment(favor.getTaskTypeString());
        showActionFragment();
    }

    private void instantiateTextFields() {
        requestTypeTV = findViewById(R.id.request_type_TV);
        statusTV = findViewById(R.id.status_TV);
        enquirerTV = findViewById(R.id.enquirer_TV);
        accepterTV = findViewById(R.id.accpter_TV);
    }

    private void fillTextFields() {
        if (favor.getTaskTypeString() != null) {
            requestTypeTV.setText(favor.getTaskTypeString());
        }
        if (favor.getStatusString() != null) {
            statusTV.setText(favor.getStatusString());
        }
        if (favor.getEnquirerName() != null) {
            enquirerTV.setText(favor.getEnquirerName());
        }
        if (favor.getAccepter() != null) {
            DocumentReference docRef = db.collection("users").document(favor.getAccepter());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String username = (String) document.getData().get("name");
                            accepterTV.setText(username);
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }
    }

    public void showFavorFragment(String taskType) {
        Fragment fragment;
        switch (taskType) {
            case "Moving":
                fragment = MovingFragment.newInstance((MovingFavor) favor);
                break;
            case "Tutoring":
                fragment = TutoringFragment.newInstance((TutoringFavor) favor);
                break;
            case "Dining":
                fragment = DiningFragment.newInstance((DiningFavor) favor);
                break;
            case "Gathering":
                fragment = GatheringFragment.newInstance((GatheringFavor) favor);
                break;
            case "Borrowing":
                fragment = BorrowingFragment.newInstance((BorrowingFavor) favor);
                break;
            default:
                return;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.favorFragmentContainer, fragment).commit();
    }

    public void showActionFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (favor.getStatusString() == "Active" && favor.getEnquirer().equals(user.getUid())) {
            RequestActionFragment requestFragment = RequestActionFragment.newInstance(favor);
            requestFragment.setButtonClickListener(markCompletedListener);
            ft.replace(R.id.actionFragmentContainer, requestFragment).commit();
        } else if (favor.getStatusString() == "Active" && favor.getAccepter().equals(user.getUid())) {
            TaskActionFragment taskFragment = TaskActionFragment.newInstance(favor);
            taskFragment.setButtonClickListener(removeFulfillerListener);
            ft.replace(R.id.actionFragmentContainer, taskFragment).commit();
        } else if (favor.getStatusString() == "Open" && !favor.getEnquirer().equals(user.getUid())) {
            FavorActionFragment favorFragment = FavorActionFragment.newInstance(favor, user.getUid());
            favorFragment.setButtonClickListener(assignFavorListener);
            ft.replace(R.id.actionFragmentContainer, favorFragment).commit();
        }
    }

    private void createListeners() {
        markCompletedListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("favors")
                        .document(favor.getId())
                        .update("status", "COMPLETED")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                statusTV.setText("Completed");
                                favor.setStatus(Status.COMPLETED);
                                showActionFragment();
                            }
                        });
                finish();
            }
        };
        removeFulfillerListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("favors")
                        .document(favor.getId())
                        .update(
                                "status", "OPEN",
                                "accepter", null
                        ).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        statusTV.setText("Open");
                        favor.setStatus(Status.OPEN);
                        accepterTV.setText("Task not yet accepted");
                        favor.setAccepter(null);
                        showActionFragment();
                    }
                });
                finish();
            }
        };
        assignFavorListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("favors")
                        .document(favor.getId())
                        .update(
                                "status", "ACTIVE",
                                "accepter", user.getUid()
                        ).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        statusTV.setText("Active");
                        favor.setStatus(Status.ACTIVE);
                        DocumentReference docRef = db.collection("users").document(user.getUid());
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        String username = (String) document.getData().get("name");
                                        accepterTV.setText(username);
                                    }
                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
                                }
                            }
                        });
                        favor.setAccepter(user.getUid());
                        showActionFragment();
                    }
                });
                finish();
            }
        };
    }

}