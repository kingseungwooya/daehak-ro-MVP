package daehakro.server.domain.admin.controller.dto.response;

import daehakro.server.domain.member.UnivInfo;
import lombok.Getter;

@Getter
public class UnivInfoDto {
    private final Long univId;

    private final String univName;

    private final String mail;



    public UnivInfoDto(UnivInfo univInfo) {
        this.univId = univInfo.getUnivId();
        this.univName = univInfo.getName();
        this.mail = univInfo.getMail();
    }
}
