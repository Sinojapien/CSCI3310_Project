package edu.cuhk.csci3310.project.model;

import android.graphics.Bitmap;

// import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Map;

import edu.cuhk.csci3310.project.database.Status;
import edu.cuhk.csci3310.project.database.TaskType;

public class TutoringFavor extends Favor {
    private LatLng location;
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private String description;
    private List<String> selection;
    private Bitmap picture;
    private String courseCode;
    private int courseParticipant;

    public TutoringFavor() { super(); }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getSelection() {
        return selection;
    }

    public void setSelection(List<String> selection) {
        this.selection = selection;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public int getCourseParticipant() {
        return courseParticipant;
    }

    public void setCourseParticipant(int courseParticipant) {
        this.courseParticipant = courseParticipant;
    }

    public static TutoringFavor createTutoringFavorFromDB(String id, Map<String, Object> data) throws Exception {
        TutoringFavor favor = new TutoringFavor();
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
        if(data.get("startDate") != null) {
            favor.setStartDate((String) data.get("startDate"));
        }
        if(data.get("endDate") != null) {
            favor.setEndDate((String) data.get("endDate"));
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
        if(data.get("selection") != null) {
            favor.setSelection((List<String>) data.get("selection"));
        }
        if(data.get("courseCode") != null) {
            favor.setCourseCode((String) data.get("courseCode"));
        }
        if(data.get("courseParticipant") != null) {
            favor.setCourseParticipant(((Long) data.get("courseParticipant")).intValue());
        }
        return favor;
    }
}
