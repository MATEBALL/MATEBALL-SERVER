package at.mateball.domain.group.core.calculator.common;

import at.mateball.domain.group.api.dto.base.GroupMatchBaseRes;

import java.util.ArrayList;
import java.util.List;

public class GroupMatchAggregate {

    private final Long matchId;
    private final Long leaderId;
    private final String nickname;
    private final boolean isGroup;
    private final List<String> images = new ArrayList<>();

    private int count;
    private int scoreSum;
    private int scoreCount;

    public GroupMatchAggregate(Long matchId, Long leaderId, String nickname, boolean isGroup) {
        this.matchId = matchId;
        this.leaderId = leaderId;
        this.nickname = nickname;
        this.isGroup = isGroup;
    }

    public void addImage(String imageUrl) {
        images.add(imageUrl);
        count++;
    }

    public void addScore(int score) {
        scoreSum += score;
        scoreCount++;
    }

    public Long getLeaderId() {
        return leaderId;
    }

    public GroupMatchBaseRes toResponse() {
        Integer matchRate = null;

        if (scoreCount > 0) {
            matchRate = (int) Math.round((double) scoreSum / scoreCount);
        }

        return new GroupMatchBaseRes(
                matchId,
                nickname,
                count,
                isGroup,
                matchRate,
                List.copyOf(images)
        );
    }
}
