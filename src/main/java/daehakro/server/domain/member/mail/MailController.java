package daehakro.server.domain.member.mail;

import daehakro.server.domain.member.controller.MemberController;
import daehakro.server.domain.member.mail.dto.request.EmailAuthRequestDto;
import daehakro.server.domain.member.mail.dto.request.EmailVerifyDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(MemberController.REST_URL_MEMBER)
public class MailController {

    public static final String REST_URL_MEMBER = "api/mvp/mail";

    private final EmailService emailService;

    @PostMapping("/")
    public ResponseEntity sendMail(@RequestBody EmailVerifyDto emailVerifyDto) {

        emailService.sendMail(emailVerifyDto);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "이메일 인증", notes = "이메일 인증을 진행한다.")
    @GetMapping("/confirm-mail")
    public ResponseEntity<?> confirmEmail(@ModelAttribute EmailAuthRequestDto requestDto) {
        emailService.verifyEmail(requestDto);
        return ResponseEntity.ok().build();
    }
}
