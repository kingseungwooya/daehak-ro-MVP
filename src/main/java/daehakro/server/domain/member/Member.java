package daehakro.server.domain.member;

import daehakro.server.domain.event.EventLog;
import daehakro.server.domain.member.enums.MemberSex;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id
    @Column(name = "member_id")
    private String memberId;

    @Column(name = "member_name")
    private String memberName;

    @Enumerated(EnumType.STRING)
    private MemberSex sex;

    private int coin;

    @Column(name = "certify_flag", nullable = false, columnDefinition = "TINYINT", length = 1)
    private boolean isCertify;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "univ_id")
    private UnivInfo univInfo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL)
    List<EventLog> eventLogs = new ArrayList<>();
    // ExcludedDepartment 객체는 event 종료시 모두 사라진다. 그에 맞게 Persist로 변경하여 save 시 영속성 보장을 해주고 orphanRemoval을 통해 delete시 같이 제거되도록 한다
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member",cascade = CascadeType.PERSIST, orphanRemoval = true)
    List<ExcludedDepartment> excludedDepartments = new ArrayList<>();


    @Builder
    public Member(String memberId, String memberName, MemberSex sex,  UnivInfo univInfo) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.sex = sex;
        this.coin = 1;
        this.isCertify = false;
        this.univInfo = univInfo;
    }

    public void applyEvent(EventLog eventLog) {
        this.eventLogs.add(eventLog);
    }

    public void useCoin() {
        this.coin = coin - 1;
    }

    public boolean haveCoin() {
        if (this.coin > 0) {
            return true;
        }
        return false;
    }

    public void addExcDepartment(ExcludedDepartment excludedDepartment) {
        this.excludedDepartments.add(excludedDepartment);
    }

}
