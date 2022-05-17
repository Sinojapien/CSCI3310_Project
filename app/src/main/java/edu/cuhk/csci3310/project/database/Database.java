package edu.cuhk.csci3310.project.database;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.net.URI;
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
    private static final String TAG = "Database";

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static CollectionReference favors = db.collection("favors");

    public static void createNewFavor(Favor favor) throws Exception{
        /*if(favor.getTaskType() == TaskType.MOVING) {
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
        return true;*/
        favors.add(favor);
    }

    public static void createNewFavorAndSaveImage(Favor favor, Bitmap image) {
        favors.add(favor)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String id = documentReference.getId();
                        saveImageInDB(image, id);
                    }
                });
    }

    public static void saveImageInDB(Bitmap bitmap, String imageName) {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // Create a storage reference from our app
        StorageReference storageRef = storage.getReferenceFromUrl("gs://cuhk-favor.appspot.com");

        // Create a reference to "mountains.jpg"
        StorageReference imageRef = storageRef.child(imageName + ".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                // Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });
    }
}
