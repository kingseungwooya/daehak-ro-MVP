package daehakro.server.domain.member.mail;

import daehakro.server.domain.member.Member;
import daehakro.server.domain.member.UnivInfo;
import daehakro.server.domain.member.enums.Department;
import daehakro.server.domain.member.mail.dto.request.EmailAuthRequestDto;
import daehakro.server.domain.member.mail.dto.request.EmailVerifyDto;
import daehakro.server.domain.member.mail.util.MailUtils;
import daehakro.server.domain.member.repo.MemberRepository;
import daehakro.server.domain.member.repo.UnivRepository;
import daehakro.server.domain.member.service.MemberService;
import daehakro.server.global.exception.ResponseEnum;
import daehakro.server.global.exception.handler.CustomApiException;

import java.time.LocalDateTime;
import java.util.Optional;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final static Logger logger = LoggerFactory.getLogger(EmailService.class);
    private static final String HTML_SOURCE = "email";
    private static final String MAIL_SUBJECT = "안녕하세요. 대학로입니다. 학교 인증을 위한 메일입니다..";

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final EmailTokenRepository emailTokenRepository;
    private final MemberRepository memberRepository;
    private final UnivRepository univRepository;


    @Value("${mail.redirect.page}")
    private String api;

    // type = email
    @Transactional
    public void sendMail(EmailVerifyDto emailVerifyDto) {
        Member member = memberRepository.findById(emailVerifyDto.getUId())
                .orElseThrow(
                        () -> new CustomApiException(ResponseEnum.USER_NOT_FOUND)
                );
        // mail 중복 확인
        if (univRepository.existsByMail(emailVerifyDto.getEmail())) {
            throw new CustomApiException(ResponseEnum.USER_MAIL_DUPLICATE);
        }

        // 이메일 토큰 저장
        EmailToken emailToken = EmailToken.createEmailToken(emailVerifyDto);
        emailTokenRepository.save(emailToken);

        // 메일 보내기
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailVerifyDto.getEmail()); // 메일 수신자
            mimeMessageHelper.setSubject(MAIL_SUBJECT); // 메일 제목
            mimeMessageHelper.setText(setContext(emailToken.getId(), api, HTML_SOURCE), true); // 메일 본문 내용, HTML 여부
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            logger.info("email 전송중 문제 생김 -> {}", e.getMessage());
            throw new CustomApiException(ResponseEnum.USER_MAIL_VERIFY_FAIL);

        } catch (Exception e) {
            logger.info("email 전송중 문제 생김 -> {}", e.getMessage());
            throw new CustomApiException(ResponseEnum.USER_MAIL_VERIFY_FAIL);
        }

    }


    // thymeleaf를 통한 html 적용
    public String setContext(String code, String api, String type) {
        Context context = new Context();
        context.setVariable("code", code);
        context.setVariable("api", api);
        return templateEngine.process(type, context);
    }

    @Transactional
    public void verifyEmail(String authToken) {
        // 이메일 토큰을 찾아옴
        EmailToken findEmailToken = findByIdAndExpirationDateAfterAndExpired(authToken);

        // 토큰의 유저 ID를 이용하여 유저 인증 정보를 가져온다.
        Member member = memberRepository.findById(findEmailToken.getMemberId())
                .orElseThrow(
                        () -> new CustomApiException(ResponseEnum.USER_NOT_FOUND)
                );
        // mail 정보로 member univ info 저장
        UnivInfo univInfo = univRepository.save(
                UnivInfo.builder()
                        .member(member)
                        .mail(findEmailToken.getEmail())
                        .name(findEmailToken.getUnivName())
                        .department(Department.of(findEmailToken.getDepartment()))
                        .build()
        );
        member.verify(univInfo);
        memberRepository.save(member);
        findEmailToken.setTokenToUsed();    // 사용 완료
    }

    // 유효한 토큰 가져오기
    private EmailToken findByIdAndExpirationDateAfterAndExpired(String token) {
        // 토큰이 없다면 예외 발생
        return emailTokenRepository.findByIdAndExpirationDateAfterAndExpired(token, LocalDateTime.now(), false)
                .orElseThrow(
                        () -> new CustomApiException(ResponseEnum.USER_MAIL_VERIFY_FAIL)
                );
    }

}
