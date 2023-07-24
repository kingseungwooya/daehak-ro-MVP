package daehakro.server.domain.member.mail;

import java.time.LocalDateTime;
import javax.persistence.*;

import daehakro.server.domain.member.enums.Department;
import daehakro.server.domain.member.mail.dto.request.EmailVerifyDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailToken {

    private static final long EMAIL_TOKEN_EXPIRATION_TIME_VALUE = 5L;    // 이메일 토큰 만료 시간

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(length = 36)
    private String id;

    private LocalDateTime expirationDate;

    private boolean expired;

    private String email;

    private String univName;

    @Enumerated(EnumType.STRING)
    private Department department;

    private String memberId;


    // 이메일 인증 토큰 생성
    public static EmailToken createEmailToken(EmailVerifyDto dto) {
        EmailToken emailToken = new EmailToken();
        emailToken.expirationDate = LocalDateTime.now().plusMinutes(EMAIL_TOKEN_EXPIRATION_TIME_VALUE); // 5분 후 만료
        emailToken.expired = false;
        emailToken.memberId = dto.getUId();
        emailToken.email = dto.getEmail();
        emailToken.department = dto.getDepartment();
        emailToken.univName = dto.getUnivName();

        return emailToken;
    }

    // 토큰 만료
    public void setTokenToUsed() {
        this.expired = true;
    }


}