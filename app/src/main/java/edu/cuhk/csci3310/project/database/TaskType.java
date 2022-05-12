package edu.cuhk.csci3310.project.database;

import com.google.android.gms.tasks.Task;

public enum TaskType {
    MOVING(0), TUTORING(1), DINING(2), GATHERING(3), BORROWING(4);

    private final int value;

    private TaskType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static TaskType getTaskTypeFromValue(int value) {
        switch(value) {
            case 0:
                return TaskType.MOVING;
            case 1:
                return TaskType.TUTORING;
            case 2:
                return TaskType.DINING;
            case 3:
                return TaskType.GATHERING;
            case 4:
                return TaskType.BORROWING;
            default:
                return null;
        }
    }
}
