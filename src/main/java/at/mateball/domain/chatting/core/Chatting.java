package at.mateball.domain.chatting.core;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "chatting")
public class Chatting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000, nullable = false)
    private String chattingUrl;

    @Column(nullable = false)
    private Boolean isUsed = false;

    protected Chatting() {
    }

    public void updateIsUsedStatus() {
        this.isUsed = !isUsed;
    }
}
