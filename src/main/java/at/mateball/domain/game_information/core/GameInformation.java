package at.mateball.domain.game_information.core;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Table(name = "game_information")
public class GameInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 45, nullable = false)
    private String awayTeamName;

    @Column(length = 45, nullable = false)
    private String homeTeamName;

    @Column(nullable = false)
    private LocalDate gameDate;

    @Column(nullable = false)
    private LocalTime gameTime;

    @Column(length = 45, nullable = false)
    private String stadiumName;

    protected GameInformation() {
    }

    public GameInformation(String awayTeamName, String homeTeamName, LocalDate gameDate, LocalTime gameTime, String stadiumName) {
        this.awayTeamName = awayTeamName;
        this.homeTeamName = homeTeamName;
        this.gameDate = gameDate;
        this.gameTime = gameTime;
        this.stadiumName = stadiumName;
    }
}
