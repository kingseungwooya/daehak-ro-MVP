package daehakro.server.domain.event;

import daehakro.server.domain.event.enums.EventType;
import daehakro.server.domain.member.UnivInfo;
import daehakro.server.domain.member.Member;
import daehakro.server.domain.member.enums.MemberSex;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
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
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Event {

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "univ_id")
    private UnivInfo univInfo;

    // 하나의 event 당 신청자들의 정보를 갖고있는다.
    @OneToMany(fetch = FetchType.LAZY)
    private List<Member> membersOfWomen = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    private List<Member> membersOfMan = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @Builder
    public Event(String eventName, LocalDate startDate, LocalDate endDate, int maxApply, UnivInfo univInfo, EventType eventType) {
        this.eventName = eventName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxApply = maxApply;
        // 생성 즉시 open 할 것인가?
        this.isOpen = true;
        this.univInfo = univInfo;
        this.eventType = eventType;
    }

    public void close() {
        this.isOpen = false;
    }

    public void apply(Member member) {
        if (member.getSex().equals(MemberSex.MAN)) {
            membersOfMan.add(member);
        } else {
            membersOfWomen.add(member);
        }
    }

    public boolean isFull(MemberSex sex) {
        if(sex.equals(MemberSex.MAN)) {
            return membersOfMan.size() == maxApply;
        }
        if(sex.equals(MemberSex.WOMAN)) {
            return membersOfWomen.size() == maxApply;
        }
        return false;
    }
}
