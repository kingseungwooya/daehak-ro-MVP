package daehakro.server.domain.member.controller.dto.converter;

import daehakro.server.domain.member.enums.MemberSex;
import org.springframework.core.convert.converter.Converter;

public class MemberSexConverter implements Converter<String, MemberSex> {
    @Override
    public MemberSex convert(String source) {
        return MemberSex.of(source);
    }
}