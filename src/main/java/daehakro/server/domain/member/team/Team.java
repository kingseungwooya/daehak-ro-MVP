package daehakro.server.domain.member.team;

import daehakro.server.domain.event.team.TeamEvent;
import daehakro.server.domain.member.ExcludedDepartment;
import daehakro.server.domain.member.Member;
import daehakro.server.domain.member.enums.MemberSex;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long teamId;

    private String applicantId;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Member> members = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private MemberSex teamSex;

    @ManyToOne(fetch = FetchType.LAZY)
    private TeamEvent event;

    private String teamName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member",cascade = CascadeType.PERSIST, orphanRemoval = true)
    List<ExcludedDepartment> excludedDepartments = new ArrayList<>();

    @Builder
    public Team(List<Member> members, MemberSex teamSex, TeamEvent event, String teamName, String applicantId) {
        this.members = members;
        this.teamSex = teamSex;
        this.event = event;
        this.teamName = teamName;
        this.applicantId = applicantId;
    }
    public void addExcDepartment(ExcludedDepartment excludedDepartment) {
        this.excludedDepartments.add(excludedDepartment);
    }
}
