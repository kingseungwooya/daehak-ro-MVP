package daehakro.server.domain.member.service;

import daehakro.server.domain.member.controller.dto.request.CheckMemberDto;
import daehakro.server.domain.member.controller.dto.request.EditDto;
import daehakro.server.domain.member.controller.dto.request.KakaoLoginDto;
import daehakro.server.domain.member.controller.dto.request.UserInfoDto;
import daehakro.server.domain.member.controller.dto.response.SignUpValidationDto;
import org.springframework.stereotype.Service;

@Service
public interface MemberService {

    boolean isAlreadyJoin(CheckMemberDto checkMemberDto);

    void editInfo(EditDto editDto);

    void userInfo(UserInfoDto userInfoDto);

    void kakaoLogin(KakaoLoginDto kakaoLoginDto);

    SignUpValidationDto validateUserAuth(CheckMemberDto checkMemberDto);
}
