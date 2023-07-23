package daehakro.server.domain.member.enums;

import daehakro.server.global.exception.ResponseEnum;
import daehakro.server.global.exception.handler.CustomApiException;
import java.util.Arrays;

public enum MemberSex {
    MAN, WOMAN;

    public static MemberSex of(String memberSex) {
        return Arrays.stream(values())
                .filter(s -> s.name().equals(memberSex))
                .findFirst()
                .orElseThrow(
                        () -> new CustomApiException(ResponseEnum.USER_JOIN_FAIL
                ));
    }
}
