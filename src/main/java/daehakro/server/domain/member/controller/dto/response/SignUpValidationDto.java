package daehakro.server.domain.member.controller.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignUpValidationDto {
    private final boolean havekakaoAuth;

    private final boolean haveUserInfo;

    private final boolean mailCertify;

    @Builder
    public SignUpValidationDto(boolean havekakaoAuth, boolean haveUserInfo, boolean mailCertify) {
        this.havekakaoAuth = havekakaoAuth;
        this.haveUserInfo = haveUserInfo;
        this.mailCertify = mailCertify;
    }
}
