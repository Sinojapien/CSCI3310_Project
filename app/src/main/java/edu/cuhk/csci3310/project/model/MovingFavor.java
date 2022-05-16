package edu.cuhk.csci3310.project.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

// import com.google.android.gms.maps.model.LatLng; // use customized LatLng instead
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import edu.cuhk.csci3310.project.database.Status;
import edu.cuhk.csci3310.project.database.TaskType;

public class MovingFavor extends Favor {
    // similar to enum, LatLng from google is not serializable, wrap another layer of class in it
    // new LatLng is in the same package
    private LatLng startLoc;
    private LatLng endLoc;
    private String date;
    private String time;
    private String description;
    private Bitmap photo;
    private byte[] photoByte; // instead of using bitmap when storing photo, use byte array instead

    public MovingFavor() {
        super();
    }

    protected MovingFavor(Parcel in) {
        super(in);
        date = in.readString();
        time = in.readString();
        description = in.readString();
        photo = in.readParcelable(Bitmap.class.getClassLoader());
        photoByte = in.createByteArray();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(description);
        dest.writeParcelable(photo, flags);
        dest.writeByteArray(photoByte);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MovingFavor> CREATOR = new Creator<MovingFavor>() {
        @Override
        public MovingFavor createFromParcel(Parcel in) {
            return new MovingFavor(in);
        }

        @Override
        public MovingFavor[] newArray(int size) {
            return new MovingFavor[size];
        }
    };

    public LatLng getStartLoc() {
        return startLoc;
    }

    public void setStartLoc(LatLng startLoc) {
        this.startLoc = startLoc;
    }

    public LatLng getEndLoc() {
        return endLoc;
    }

    public void setEndLoc(LatLng endLoc) {
        this.endLoc = endLoc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Exclude // excluded because firebase does not support bitmap storage
    public Bitmap getPhoto() {
        return photo;
    }

    @Exclude
    public void setPhoto(Bitmap photo) {
        // now set the byteArray too
        this.photo = photo;
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
//        this.photoByte =  stream.toByteArray();
    }

    // getter and setter for firebase to save and toObject()
    public byte[] getPhotoByte(){
        return this.photoByte;
    }
    public void setPhotoByte(byte[] photoByte){
        // set the photo field too
        this.photoByte = photoByte;
//        this.photo = BitmapFactory.decodeByteArray(photoByte, 0, photoByte.length);
    }

    public static MovingFavor createMovingFavorFromDB(String id, Map<String, Object> data) throws Exception {
        MovingFavor favor = new MovingFavor();
        if(id != null) {
            favor.setId(id);
        }
        if(data.get("enquirer") != null) {
            favor.setEnquirer((String) data.get("enquirer"));
        }
        if(data.get("accepter") != null) {
            favor.setAccepter((String) data.get("accepter"));
        }
        if(data.get("taskType") != null) {
            favor.setTaskType(TaskType.getTaskTypeFromValue(((Long) data.get("taskType")).intValue()));
        }
        if(data.get("status") != null) {
            favor.setStatus(Status.getStatusFromValue(((Long) data.get("status")).intValue()));
        }
        if(data.get("startLoc") != null) {
            Map<String, Object> loc = (Map<String, Object>) data.get("startLoc");
            double latitude = 0;
            double longitude = 0;
            if(loc.get("lat") != null) {
                latitude = (double) loc.get("lat");
            }
            if(loc.get("long") != null) {
                longitude = (double) loc.get("long");
            }
            favor.setStartLoc(new LatLng(latitude, longitude));
        }
        if(data.get("endLoc") != null) {
            Map<String, Object> loc = (Map<String, Object>) data.get("endLoc");
            double latitude = 0;
            double longitude = 0;
            if(loc.get("lat") != null) {
                latitude = (double) loc.get("lat");
            }
            if(loc.get("long") != null) {
                longitude = (double) loc.get("long");
            }
            favor.setEndLoc(new LatLng(latitude, longitude));
        }
        if(data.get("date") != null) {
            favor.setDate((String) data.get("date"));
        }
        if(data.get("time") != null) {
            favor.setTime((String) data.get("time"));
        }
        if(data.get("description") != null) {
            favor.setDescription((String) data.get("description"));
        }
        return favor;
    }
}
