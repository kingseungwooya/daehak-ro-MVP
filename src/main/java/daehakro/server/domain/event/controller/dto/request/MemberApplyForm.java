package daehakro.server.domain.event.controller.dto.request;

import daehakro.server.domain.member.enums.Department;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberApplyForm {

    private String memberId;

    private Long eventId;

    private List<Department> excludeDepartments;

}
