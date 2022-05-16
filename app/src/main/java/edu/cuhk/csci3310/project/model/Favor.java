package edu.cuhk.csci3310.project.model;

import com.google.firebase.firestore.Exclude;

import edu.cuhk.csci3310.project.database.Status;
import edu.cuhk.csci3310.project.database.TaskType;

import android.os.Parcel;
import android.os.Parcelable;

public class Favor implements Parcelable {
    private String id;
    private String enquirer;
    private String enquirerName;
    private String accepter;
    private String taskType;
    private String status;

    public Favor() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEnquirer() {
        return enquirer;
    }

    public void setEnquirer(String enquirer) {
        this.enquirer = enquirer;
    }

    public String getEnquirerName(){
        return this.enquirerName;
    }
    public void setEnquirerName(String enquirerName){
        this.enquirerName = enquirerName;
    }

    public String getAccepter() {
        return accepter;
    }

    public void setAccepter(String accepter) {
        this.accepter = accepter;
    }

    public TaskType getTaskType() {
        return convertTasktype(taskType);
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType.name();
    }

    public Status getStatus() {
        return convertStatus(status);
    }

    public void setStatus(Status status) {
        this.status = status.name();
    }

    public Favor(String enquirer, TaskType taskType) {
        this.enquirer = enquirer;
        this.taskType = taskType.name();
    }

    // without @Exculde, firebase will misunderstand that there exist a field called TaskTypeString
    @Exclude
    public String getTaskTypeString() {
//        switch(this.taskType) {
//            case MOVING:
//                return "Moving";
//            case TUTORING:
//                return "Tutoring";
//            case DINING:
//                return "Dining";
//            case GATHERING:
//                return "Gathering";
//            case BORROWING:
//                return "Borrowing";
//            default:
//                return null;
//        }
        switch(this.taskType) {
            case "MOVING":
                return "Moving";
            case "TUTORING":
                return "Tutoring";
            case "DINING":
                return "Dining";
            case "GATHERING":
                return "Gathering";
            case "BORROWING":
                return "Borrowing";
            default:
                return null;
        }
    }
    @Exclude
    public String getStatusString(){
//        switch(this.status) {
//            case OPEN:
//                return "Open";
//            case ACTIVE:
//                return "Active";
//            case COMPLETED:
//                return "Completed";
//            default:
//                return null;
//        }
        switch(this.status) {
            case "OPEN":
                return "Open";
            case "ACTIVE":
                return "Active";
            case "COMPLETED":
                return "Completed";
            default:
                return null;
        }
    }

    // converting favor internal tasktype(String) to tasktype(TaskType)
    // for compatibility to all other code, so that only internal storage in favor need to be changed
    // the reason for changing Tasktype to String instead of enum is because
    // it make saving to db much, much easier (by calling collectionRef.add(MovingFavor favor)
    private TaskType convertTasktype(String s){
        switch(s) {
            case "MOVING":
                return TaskType.MOVING;
            case "TUTORING":
                return TaskType.TUTORING;
            case "DINING":
                return TaskType.DINING;
            case "GATHERING":
                return TaskType.GATHERING;
            case "BORROWING":
                return TaskType.BORROWING;
            default:
                return null;
        }
    }
    // same for status
    private Status convertStatus(String s){
        switch (s) {
            case "OPEN":
                return Status.OPEN;
            case "ACTIVE":
                return Status.ACTIVE;
            case "COMPLETED":
                return Status.COMPLETED;
            default:
                return null;
        }
    }

    // Parcelable is useful for passing object between activity, also much faster than java native serialization
    // Reference: stack overflow question
    // https://stackoverflow.com/questions/2139134/how-to-send-an-object-from-one-android-activity-to-another-using-intents
    // Parcelable required method.
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(id);
        out.writeString(enquirer);
        out.writeString(enquirerName);
        out.writeString(accepter);
        out.writeString(taskType);
        out.writeString(status);
    }

    // constructor the object back
    public static final Parcelable.Creator<Favor> CREATOR = new Parcelable.Creator<Favor>() {
        public Favor createFromParcel(Parcel in) {
            return new Favor(in);
        }

        public Favor[] newArray(int size) {
            return new Favor[size];
        }
    };

    // constructor for Parcelable, only used privately and for child
    protected Favor(Parcel in) {
        // first in, first out manner
        this.id = in.readString();
        this.enquirer = in.readString();
        this.enquirerName = in.readString();
        this.accepter = in.readString();
        this.taskType = in.readString();
        this.status = in.readString();
    }
}