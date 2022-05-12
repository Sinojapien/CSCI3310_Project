package edu.cuhk.csci3310.project.database;

public enum TaskType {
    MOVING(0), TUTORING(1), DINING(2), GATHERING(3), BORROWING(4);

    private final int value;

    private TaskType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
