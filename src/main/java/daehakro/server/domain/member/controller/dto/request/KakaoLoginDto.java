package daehakro.server.domain.member.controller.dto.request;

import daehakro.server.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KakaoLoginDto {

    private String uId;

    private String kakaoKey;

    public Member toMember() {
        return new Member(this.uId, this.kakaoKey);
    }
}
