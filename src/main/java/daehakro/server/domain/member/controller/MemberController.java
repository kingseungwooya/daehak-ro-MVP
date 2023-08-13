package daehakro.server.domain.member.controller;

import daehakro.server.domain.member.controller.dto.request.CheckMemberDto;
import daehakro.server.domain.member.controller.dto.request.EditDto;
import daehakro.server.domain.member.controller.dto.request.KakaoLoginDto;
import daehakro.server.domain.member.controller.dto.request.UserInfoDto;
import daehakro.server.domain.member.controller.dto.response.SignUpValidationDto;
import daehakro.server.domain.member.mail.dto.request.EmailAuthRequestDto;
import daehakro.server.domain.member.repo.MemberRepository;
import daehakro.server.domain.member.service.MemberService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping(MemberController.REST_URL_MEMBER)
public class MemberController {

    public static final String REST_URL_MEMBER = "api/mvp/user";

    private final MemberService memberService;


    /**
     * kakao login
     */
    @PostMapping("/kakao-register")
    public ResponseEntity<Void> kakaoLogin(@RequestBody KakaoLoginDto kakaoLoginDto) {
        memberService.kakaoLogin(kakaoLoginDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    /**
     * info 작성
     */
    @PostMapping("/user-info")
    public ResponseEntity<Void> userInfo(@RequestBody UserInfoDto userInfoDto) {
        memberService.userInfo(userInfoDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 유저 검증
     */
    @PostMapping("/auth-check")
    public ResponseEntity<SignUpValidationDto> authValidate(@RequestBody CheckMemberDto checkMemberDto) {
        return new ResponseEntity<>(memberService.validateUserAuth(checkMemberDto), HttpStatus.OK);
    }

    /**
     * 회원 정보 수정
     */
    @PutMapping("/user-info")
    public ResponseEntity<?> editUserInfo(@RequestBody EditDto editDto) {
        memberService.editInfo(editDto);
        return ResponseEntity.ok().build();
    }


    @Autowired
    MemberRepository memberRepository;
    @DeleteMapping("/delete")
    public void deleteMember(@RequestParam String memberId) {
        memberRepository.deleteById(memberId);
    }

}