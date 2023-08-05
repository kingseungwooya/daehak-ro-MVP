package daehakro.server.domain.admin.controller.dto.response;

import daehakro.server.domain.member.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MatchedMemberDto {

    private final String name;
    private final String memberSex;
    private final String uId;
    private final String kakadId;
    private final String department;
    private final String univName;

    @Builder
    public MatchedMemberDto(Member member) {
        this.name = member.getMemberName();
        this.memberSex = member.getSex().name();
        this.uId = member.getMemberId();
        this.kakadId = member.getKakaoId();
        this.department = member.getUnivInfo().getDepartment().name();
        this.univName = member.getUnivInfo().getName();
    }
}
