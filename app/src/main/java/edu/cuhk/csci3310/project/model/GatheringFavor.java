package edu.cuhk.csci3310.project.model;

// import com.google.android.gms.maps.model.LatLng;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

import edu.cuhk.csci3310.project.database.Status;
import edu.cuhk.csci3310.project.database.TaskType;

public class GatheringFavor extends Favor {
    private LatLng location;
    private String description;
    private String date;
    private String startTime;
    private String endTime;
    private String activityType;
    private int participant;

    public GatheringFavor() {
        super();
    }

    protected GatheringFavor(Parcel in) {
        super(in);
        location = in.readParcelable(LatLng.class.getClassLoader());
        description = in.readString();
        date = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        activityType = in.readString();
        participant = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(location, flags);
        dest.writeString(description);
        dest.writeString(date);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(activityType);
        dest.writeInt(participant);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GatheringFavor> CREATOR = new Creator<GatheringFavor>() {
        @Override
        public GatheringFavor createFromParcel(Parcel in) {
            return new GatheringFavor(in);
        }

        @Override
        public GatheringFavor[] newArray(int size) {
            return new GatheringFavor[size];
        }
    };

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public int getParticipant() {
        return participant;
    }

    public void setParticipant(int participant) {
        this.participant = participant;
    }

    public static Favor createGatheringFavorFromDB(String id, Map<String, Object> data) {
        GatheringFavor favor = new GatheringFavor();
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
        if(data.get("startTime") != null) {
            favor.setStartTime((String) data.get("startTime"));
        }
        if(data.get("endTime") != null) {
            favor.setEndTime((String) data.get("endTime"));
        }
        if(data.get("description") != null) {
            favor.setDescription((String) data.get("description"));
        }
        if(data.get("activityType") != null) {
            favor.setActivityType((String) data.get("activityType"));
        }
        if(data.get("participant") != null) {
            favor.setParticipant(((Long) data.get("participant")).intValue());
        }
        return favor;
    }
}
