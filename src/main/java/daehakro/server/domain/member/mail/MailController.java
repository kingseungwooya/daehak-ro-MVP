package daehakro.server.domain.member.mail;

import daehakro.server.domain.member.mail.dto.request.EmailAuthRequestDto;
import daehakro.server.domain.member.mail.dto.request.EmailVerifyDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(MailController.REST_URL_MAIL)
public class MailController {

    public static final String REST_URL_MAIL = "api/mvp/mail";

    private final EmailService emailService;

    @PostMapping("/")
    public ResponseEntity sendMail(@RequestBody EmailVerifyDto emailVerifyDto) {
        emailService.sendMail(emailVerifyDto);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "이메일 인증", notes = "이메일 인증을 진행한다.")
    @GetMapping("/confirm-mail")
    public String confirmEmail(@RequestParam(name = "code") String authToken) {
        emailService.verifyEmail(authToken);
        return "인증 성공하였습니다. 창을 닫을주세요";
    }
}
