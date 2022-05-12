package edu.cuhk.csci3310.project.database;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import edu.cuhk.csci3310.project.model.BorrowingFavor;
import edu.cuhk.csci3310.project.model.DiningFavor;
import edu.cuhk.csci3310.project.model.Favor;
import edu.cuhk.csci3310.project.model.GatheringFavor;
import edu.cuhk.csci3310.project.model.MovingFavor;
import edu.cuhk.csci3310.project.model.TutoringFavor;

public class Database {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final String TAG = "Database";

    public static boolean createNewFavor(Favor favor) throws Exception{
        if(favor.getTaskType() == TaskType.MOVING) {
            MovingFavor movingFavor = (MovingFavor) favor;
            createNewMovingFavor(movingFavor);
        } else if(favor.getTaskType() == TaskType.TUTORING) {
            TutoringFavor tutoringFavor = (TutoringFavor) favor;
            createNewTutoringFavor(tutoringFavor);
        } else if(favor.getTaskType() == TaskType.DINING) {
            DiningFavor diningFavor = (DiningFavor) favor;
            createNewDiningFavor(diningFavor);
        } else if(favor.getTaskType() == TaskType.GATHERING) {
            GatheringFavor gatheringFavor = (GatheringFavor) favor;
            createNewGatheringFavor(gatheringFavor);
        } else if(favor.getTaskType() == TaskType.BORROWING) {
            BorrowingFavor borrowingFavor = (BorrowingFavor) favor;
            createNewBorrowingFavor(borrowingFavor);
        }
        return true;
    }

    private static void createNewMovingFavor(MovingFavor favor) throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("enquirer", favor.getEnquirer());
        data.put("accepter", favor.getAccepter());
        data.put("taskType", favor.getTaskType().getValue());
        data.put("status", favor.getStatus().getValue());
        Map<String, Object> startLoc = new HashMap<>();
        startLoc.put("lat", favor.getStartLoc().latitude);
        startLoc.put("long", favor.getStartLoc().longitude);
        data.put("startLoc", startLoc);
        Map<String, Object> endLoc = new HashMap<>();
        endLoc.put("lat", favor.getEndLoc().latitude);
        endLoc.put("long", favor.getEndLoc().longitude);
        data.put("endLoc", endLoc);
        data.put("date", favor.getDate());
        data.put("time", favor.getTime());
        data.put("description", favor.getDescription());
        // Save favor in DB
        saveFavorInDB(data);

    }

    private static void createNewTutoringFavor(TutoringFavor favor) throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("enquirer", favor.getEnquirer());
        data.put("accepter", favor.getAccepter());
        data.put("taskType", favor.getTaskType().getValue());
        data.put("status", favor.getStatus().getValue());
        Map<String, Object> loc = new HashMap<>();
        loc.put("lat", favor.getLocation().latitude);
        loc.put("long", favor.getLocation().longitude);
        data.put("loc", loc);
        data.put("description", favor.getDescription());
        data.put("startDate", favor.getStartDate());
        data.put("endDate", favor.getEndDate());
        data.put("startTime", favor.getStartTime());
        data.put("endTime", favor.getStartTime());
        data.put("selection", favor.getSelection());
        data.put("courseCode", favor.getCourseCode());
        data.put("courseParticipant", favor.getCourseParticipant());

        // Save favor in DB
        saveFavorInDB(data);
    }

    private static void createNewDiningFavor(DiningFavor favor) throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("enquirer", favor.getEnquirer());
        data.put("accepter", favor.getAccepter());
        data.put("taskType", favor.getTaskType().getValue());
        data.put("status", favor.getStatus().getValue());
        Map<String, Object> loc = new HashMap<>();
        loc.put("lat", favor.getLocation().latitude);
        loc.put("long", favor.getLocation().longitude);
        data.put("loc", loc);
        data.put("description", favor.getDescription());
        data.put("date", favor.getDate());
        data.put("startTime", favor.getStartTime());
        data.put("endTime", favor.getStartTime());
        data.put("participant", favor.getParticipant());

        // Save favor in DB
        saveFavorInDB(data);
    }

    private static void createNewGatheringFavor(GatheringFavor favor) throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("enquirer", favor.getEnquirer());
        data.put("accepter", favor.getAccepter());
        data.put("taskType", favor.getTaskType().getValue());
        data.put("status", favor.getStatus().getValue());
        Map<String, Object> loc = new HashMap<>();
        loc.put("lat", favor.getLocation().latitude);
        loc.put("long", favor.getLocation().longitude);
        data.put("loc", loc);
        data.put("description", favor.getDescription());
        data.put("date", favor.getDate());
        data.put("startTime", favor.getStartTime());
        data.put("endTime", favor.getStartTime());
        data.put("activityType", favor.getActivityType());
        data.put("participant", favor.getParticipant());

        // Save favor in DB
        saveFavorInDB(data);
    }

    private static void createNewBorrowingFavor(BorrowingFavor favor) throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("enquirer", favor.getEnquirer());
        data.put("accepter", favor.getAccepter());
        data.put("taskType", favor.getTaskType().getValue());
        data.put("status", favor.getStatus().getValue());
        Map<String, Object> loc = new HashMap<>();
        loc.put("lat", favor.getLocation().latitude);
        loc.put("long", favor.getLocation().longitude);
        data.put("loc", loc);
        data.put("description", favor.getDescription());
        data.put("date", favor.getDate());
        data.put("time", favor.getTime());
        data.put("activityType", favor.getActivityType());
        data.put("itemType", favor.getItemType());

        // Save favor in DB
        saveFavorInDB(data);
    }

    public static MovingFavor getMovingFavorById(String id) {
        DocumentReference docRef = db.collection("favors").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        return new MovingFavor();
    }

    public static void saveFavorInDB(Map<String, Object> data) throws Exception {
        // Save favor in db
        db.collection("favors")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        //getMovingFavorById(documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
}
