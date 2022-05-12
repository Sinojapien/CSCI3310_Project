package edu.cuhk.csci3310.project.database;

public enum Status {
    OPEN(0), ACTIVE(1), COMPLETED(2);

    private final int value;

    private Status(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Status getStatusFromValue(int status) {
        switch (status) {
            case 0:
                return Status.OPEN;
            case 1:
                return Status.ACTIVE;
            case 2:
                return Status.COMPLETED;
            default:
                return null;
        }
    }
}
