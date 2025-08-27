package at.mateball.domain.groupmember.api.dto;

import java.util.List;

public class DirectStatusListRes<T> {
    private final List<T> results;

    public DirectStatusListRes(List<T> results) {
        this.results = results;
    }

    public List<T> getResults() {
        return results;
    }
}
