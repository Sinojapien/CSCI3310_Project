package edu.cuhk.csci3310.project.model;

import edu.cuhk.csci3310.project.database.Status;
import edu.cuhk.csci3310.project.database.TaskType;

public class Favor {
    private String id;
    private String enquirer;
    private String accepter;
    private TaskType taskType;
    private Status status;

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

    public String getAccepter() {
        return accepter;
    }

    public void setAccepter(String accepter) {
        this.accepter = accepter;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Favor(String enquirer, TaskType taskType) {
        this.enquirer = enquirer;
        this.taskType = taskType;
    }
}