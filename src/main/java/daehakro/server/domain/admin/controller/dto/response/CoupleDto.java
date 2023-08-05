package daehakro.server.domain.admin.controller.dto.response;

import java.util.List;
import lombok.Getter;

@Getter
public class CoupleDto {
    private final Long coupleId;
    private final List<MatchedMemberDto> matchedMembers;

    public CoupleDto(List<MatchedMemberDto> matchedMembers, Long coupleId) {
        this.matchedMembers = matchedMembers;
        this.coupleId = coupleId;
    }
}
