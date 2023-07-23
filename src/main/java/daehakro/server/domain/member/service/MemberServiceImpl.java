package daehakro.server.domain.member.service;

import daehakro.server.domain.member.Member;
import daehakro.server.domain.member.controller.dto.request.CheckMemberDto;
import daehakro.server.domain.member.controller.dto.request.KakaoLoginDto;
import daehakro.server.domain.member.controller.dto.request.UserInfoDto;
import daehakro.server.domain.member.controller.dto.response.SignUpValidationDto;
import daehakro.server.domain.member.repo.MemberRepository;
import daehakro.server.global.exception.ResponseEnum;
import daehakro.server.global.exception.handler.CustomApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public boolean isAlreadyJoin(CheckMemberDto checkMemberDto) {
        return false;
    }

    @Override
    public void userInfo(UserInfoDto userInfoDto) {
        Member member = memberRepository.findById(userInfoDto.getUId()).orElseThrow(
                () -> new CustomApiException(ResponseEnum.USER_NOT_FOUND)
        );
        member.updateInfo(userInfoDto);
    }

    @Override
    public void kakaoLogin(KakaoLoginDto kakaoLoginDto) {
        if (memberRepository.existsByMemberIdAndKakaoKey(kakaoLoginDto.getUId(), kakaoLoginDto.getKakaoKey())) {
            throw new CustomApiException(ResponseEnum.USER_KAKAO_INFO_EXIST);
        }
        memberRepository.save(kakaoLoginDto.toMember());
        // throw  new CustomApiException(ResponseEnum.USER_FIRST_VERIFY_SUCCESS);
    }

    @Override
    public SignUpValidationDto validateUserAuth(CheckMemberDto checkMemberDto) {
        if (!memberRepository.existsById(checkMemberDto.getUId())) {
            return SignUpValidationDto.builder()
                    .haveUserInfo(false)
                    .havekakaoAuth(false)
                    .mailCertify(false)
                    .build();
        }
        Member member = memberRepository.findById(checkMemberDto.getUId()).get();
        if (!member.isHaveInfo()) {
            return SignUpValidationDto.builder()
                    .havekakaoAuth(true)
                    .haveUserInfo(false)
                    .mailCertify(false)
                    .build();
        }
        if (!member.isCertify()) {
            return SignUpValidationDto.builder()
                    .havekakaoAuth(true)
                    .haveUserInfo(true)
                    .mailCertify(false)
                    .build();
        }
        return SignUpValidationDto.builder()
                .havekakaoAuth(true)
                .haveUserInfo(true)
                .mailCertify(true)
                .build();
    }
}
