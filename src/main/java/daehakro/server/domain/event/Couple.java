package daehakro.server.domain.event;


import daehakro.server.domain.event.enums.EventType;
import daehakro.server.domain.event.team.TeamEvent;
import daehakro.server.domain.member.Member;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Couple {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "couple_id")
    private Long coupleId;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Member> members;


    private Long eventId;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @Builder
    public Couple(List<Member> members, Long eventId, EventType eventType) {
        this.members = members;
        this.eventId = eventId;
        this.eventType = eventType;
    }
}
