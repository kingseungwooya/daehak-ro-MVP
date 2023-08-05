package daehakro.server.domain.event.team;

import daehakro.server.domain.admin.controller.dto.response.EventResDto;
import daehakro.server.domain.event.enums.EventType;
import daehakro.server.domain.member.enums.MemberSex;
import daehakro.server.domain.member.team.Team;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;


@Entity
@NoArgsConstructor
@Getter
@Table(name = "team_event")
public class TeamEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long eventId;

    private String eventName;

    @CreationTimestamp
    @Column(name = "create_at", nullable = false)
    private Timestamp createAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "start_date")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "end_date")
    private LocalDate endDate;

    private int maxApply;

    @Column(name = "open_flag", nullable = false, columnDefinition = "TINYINT", length = 1)
    private boolean isOpen;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event", cascade = CascadeType.ALL)
    private List<Team> manTeams = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event", cascade = CascadeType.ALL)
    private List<Team> womenTeams = new ArrayList<>();

    @Column(name = "match_flag", nullable = false, columnDefinition = "TINYINT", length = 1)
    private boolean isMatch;

    @Builder
    public TeamEvent(String eventName, LocalDate startDate, LocalDate endDate, int maxApply, EventType eventType) {
        this.eventName = eventName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxApply = maxApply;
        // 생성 즉시 open 할 것인가?
        this.isOpen = true;
        this.isMatch = false;
        this.eventType = eventType;
    }
    public void match() {
        this.isMatch = true;
    }

    public void close() {
        this.isOpen = false;
    }

    public void applyTeam(Team team) {
        if (team.getTeamSex().equals(MemberSex.MAN)) {
            manTeams.add(team);
        } else {
            womenTeams.add(team);
        }
    }

    public boolean isDateWithinRange() {
        LocalDate currentDate = LocalDate.now();
        return !currentDate.isBefore(startDate.minusDays(1)) && !currentDate.isAfter(endDate.plusDays(1));
    }

    public boolean isFull(MemberSex sex) {
        if (sex.equals(MemberSex.MAN)) {
            return manTeams.size() == maxApply;
        }
        if (sex.equals(MemberSex.WOMAN)) {
            return womenTeams.size() == maxApply;
        }
        return false;
    }

    public EventResDto to() {
        return EventResDto.builder()
                .eventId(eventId)
                .eventName(eventName)
                .eventType(eventType)
                .endDate(endDate)
                .startDate(startDate)
                .manApply(manTeams.size())
                .maxApply(maxApply)
                .womanApply(womenTeams.size())
                .match(isMatch)
                .build();
    }
}
