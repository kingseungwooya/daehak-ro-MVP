package daehakro.server.domain.member.controller.dto.request;

import daehakro.server.domain.member.enums.MemberSex;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {

    private String uId;

    private String kakaoId;

    private String nickName;

    private MemberSex memberSex;


}
