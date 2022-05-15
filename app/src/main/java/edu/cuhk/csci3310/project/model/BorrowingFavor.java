package edu.cuhk.csci3310.project.model;

// import com.google.android.gms.maps.model.LatLng;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

import edu.cuhk.csci3310.project.database.Status;
import edu.cuhk.csci3310.project.database.TaskType;

public class BorrowingFavor extends Favor {
    private String activityType;
    private String description;
    private LatLng location;
    private String date;
    private String time;

    private String itemType;

    public BorrowingFavor() {
        super();
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
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

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public static BorrowingFavor createBorrowingFavorFromDB(String id, Map<String, Object> data) {
        BorrowingFavor favor = new BorrowingFavor();
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
        if(data.get("loc") != null) {
            Map<String, Object> loc = (Map<String, Object>) data.get("loc");
            double latitude = 0;
            double longitude = 0;
            if(loc.get("lat") != null) {
                latitude = (double) loc.get("lat");
            }
            if(loc.get("long") != null) {
                longitude = (double) loc.get("long");
            }
            favor.setLocation(new LatLng(latitude, longitude));
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
        if(data.get("activityType") != null) {
            favor.setActivityType((String) data.get("activityType"));
        }
        if(data.get("itemType") != null) {
            favor.setItemType((String) data.get("itemType"));
        }
        return favor;
    }

    // Parcelable is useful for passing object between activity, also much faster than java native serialization
    // Reference: stack overflow question
    // https://stackoverflow.com/questions/2139134/how-to-send-an-object-from-one-android-activity-to-another-using-intents
    // Parcelable required method.

    @Override
    public void writeToParcel(Parcel out, int flags) {
        // write information in super class first
        super.writeToParcel(out,flags);
        // than add information related to the child class
        out.writeString(this.activityType);
        out.writeString(this.description);
        out.writeDouble(this.location.getLatitude());
        out.writeDouble(this.location.getLongitude());
        out.writeString(this.date);
        out.writeString(this.time);
    }

    // constructor the object back
    public static final Parcelable.Creator<Favor> CREATOR = new Parcelable.Creator<Favor>() {
        public BorrowingFavor createFromParcel(Parcel in) {
            return new BorrowingFavor(in);
        }

        public BorrowingFavor[] newArray(int size) {
            return new BorrowingFavor[size];
        }
    };

    // constructor for Parcelable, only used privately
    private BorrowingFavor(Parcel in) {
        // first in, first out manner
        super(in);
        this.activityType = in.readString();
        this.description = in.readString();
        Double lat = in.readDouble();
        Double lng = in.readDouble();
        this.location = new LatLng(lat, lng);
        this.date = in.readString();
        this.time = in.readString();
    }
}
