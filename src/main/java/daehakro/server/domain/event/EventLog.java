package daehakro.server.domain.event;

import daehakro.server.domain.event.enums.EventType;
import daehakro.server.domain.member.Member;
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
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@RequiredArgsConstructor
@Table(name = "event_log")
@Getter
public class EventLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId;

    @Column(name = "event_id")
    private Long eventId;


    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "close_flag", nullable = false, columnDefinition = "TINYINT", length = 1)
    private boolean isClose;

    @Column(name = "match_flag", nullable = false, columnDefinition = "TINYINT", length = 1)
    private boolean isMatch;

    private Long coupleId;

    @Builder
    public EventLog(Long eventId, Member member, EventType eventType) {
        this.eventId = eventId;
        this.member = member;
        this.isClose = false;
        this.isMatch = false;
        this.eventType = eventType;
    }

    public void match(Long coupleId) {
        this.isMatch = true;
        this.coupleId = coupleId;
    }

    public void close() {
        this.isClose = true;
    }
}
