package at.mateball.domain.group.scheduler;

public enum ScheduleType {

    FAIL,
    COMPLETE;

    public boolean isFail() {
        return this == FAIL;
    }

    public boolean isComplete() {
        return this == COMPLETE;
    }
}
