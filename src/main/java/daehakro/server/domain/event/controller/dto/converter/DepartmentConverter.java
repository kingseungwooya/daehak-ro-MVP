package daehakro.server.domain.event.controller.dto.converter;

import daehakro.server.domain.member.enums.Department;
import org.springframework.core.convert.converter.Converter;


public class DepartmentConverter implements Converter<String, Department> {
    @Override
    public Department convert(String source) {
        return Department.of(source);
    }
}
