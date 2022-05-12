package edu.cuhk.csci3310.project.model;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Map;

import edu.cuhk.csci3310.project.database.Status;
import edu.cuhk.csci3310.project.database.TaskType;

public class MovingFavor extends Favor {
    private LatLng startLoc;
    private LatLng endLoc;
    private String date;
    private String time;
    private String description;
    private Bitmap photo;

    public MovingFavor() {
        super();
    }

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

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
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
