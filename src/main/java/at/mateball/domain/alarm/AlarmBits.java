package at.mateball.domain.alarm;

public final class AlarmBits {
    private AlarmBits() {}

    public static final int NEW_REQUEST = 0b001;
    public static final int APPROVED    = 0b010;
    public static final int MATCHED     = 0b100;

    public static int from(AlarmType type) {
        return switch (type) {
            case NEW_REQUEST -> NEW_REQUEST;
            case APPROVED    -> APPROVED;
            case MATCHED     -> MATCHED;
        };
    }
}

