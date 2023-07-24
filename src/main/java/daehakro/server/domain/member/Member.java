package daehakro.server.domain.member;

import daehakro.server.domain.event.EventLog;
import daehakro.server.domain.member.controller.dto.request.UserInfoDto;
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
import javax.persistence.OneToOne;
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

    @Column(name = " kakao_key")
    private String kakaoKey;

    @Column(name = "kakao_id")
    private String kakaoId;


    @Enumerated(EnumType.STRING)
    private MemberSex sex;

    private int coin;

    @Column(name = "info_flag", nullable = false, columnDefinition = "TINYINT", length = 1)
    private boolean haveInfo;

    @Column(name = "certify_flag", nullable = false, columnDefinition = "TINYINT", length = 1)
    private boolean isCertify;

    @OneToOne
    @JoinColumn(name = "univ_id")
    private UnivInfo univInfo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL)
    List<EventLog> eventLogs = new ArrayList<>();
    // ExcludedDepartment 객체는 event 종료시 모두 사라진다. 그에 맞게 Persist로 변경하여 save 시 영속성 보장을 해주고 orphanRemoval을 통해 delete시 같이 제거되도록 한다
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member",cascade = CascadeType.PERSIST, orphanRemoval = true)
    List<ExcludedDepartment> excludedDepartments = new ArrayList<>();

    public Member(String uId, String kakaoKey) {
        this.memberId = uId;
        this.kakaoKey = kakaoKey;
        this.haveInfo = false;
        this.isCertify = false;
        this.coin = 0;
    }

    public void applyEvent(EventLog eventLog) {
        this.eventLogs.add(eventLog);
    }

    public void useCoin(int pay) {
        this.coin = coin - pay;
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

    public boolean haveCoin(int price) {
        if (this.coin >= price) {
            return true;
        }
        return false;
    }

    public void addExcDepartment(ExcludedDepartment excludedDepartment) {
        this.excludedDepartments.add(excludedDepartment);
    }


    public void updateInfo(UserInfoDto userInfoDto) {
        this.sex = userInfoDto.getMemberSex();
        this.memberName = userInfoDto.getNickName();
        this.kakaoId = userInfoDto.getKakaoId();
        this.haveInfo = true;
    }

    public void verify(UnivInfo univInfo) {
        this.isCertify = true;
        this.univInfo = univInfo;
    }

    // 학교 인증과 동시에
    public void updateUnivInfo() {

    }
}
