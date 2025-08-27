package at.mateball.domain.groupmember.api.dto;

import java.util.List;

public class GroupStatusListRes<T> {
    private final List<T> mates;

    public GroupStatusListRes(List<T> mates) {
        this.mates = mates;
    }

    public List<T> getMates() {
        return mates;
    }
}
