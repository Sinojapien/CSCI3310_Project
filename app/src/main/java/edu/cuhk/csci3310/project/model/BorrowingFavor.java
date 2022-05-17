package edu.cuhk.csci3310.project.model;

// import com.google.android.gms.maps.model.LatLng;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import java.util.Map;

import edu.cuhk.csci3310.project.database.Status;
import edu.cuhk.csci3310.project.database.TaskType;

public class BorrowingFavor extends Favor {
    private String description;
    private LatLng location;
    private String startDate;
    private String endDate;
    private String time;
    private String itemType;
    private List<String> selection;

    protected BorrowingFavor(Parcel in) {
        super(in);
        description = in.readString();
        location = in.readParcelable(LatLng.class.getClassLoader());
        startDate = in.readString();
        endDate = in.readString();
        time = in.readString();
        itemType = in.readString();
        selection = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(description);
        dest.writeParcelable(location, flags);
        dest.writeString(startDate);
        dest.writeString(endDate);
        dest.writeString(time);
        dest.writeString(itemType);
        dest.writeStringList(selection);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BorrowingFavor> CREATOR = new Creator<BorrowingFavor>() {
        @Override
        public BorrowingFavor createFromParcel(Parcel in) {
            return new BorrowingFavor(in);
        }

        @Override
        public BorrowingFavor[] newArray(int size) {
            return new BorrowingFavor[size];
        }
    };

    public List<String> getSelection() {
        return selection;
    }

    public void setSelection(List<String> selection) {
        this.selection = selection;
    }

    public static Creator<BorrowingFavor> getCREATOR() {
        return CREATOR;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public BorrowingFavor() {
        super();
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
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
            favor.setStartDate((String) data.get("date"));
        }
        if(data.get("time") != null) {
            favor.setTime((String) data.get("time"));
        }
        if(data.get("description") != null) {
            favor.setDescription((String) data.get("description"));
        }
        if(data.get("itemType") != null) {
            favor.setItemType((String) data.get("itemType"));
        }
        return favor;
    }
}
