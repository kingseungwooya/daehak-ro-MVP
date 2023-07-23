package daehakro.server.domain.event.controller.dto.request;

import daehakro.server.domain.member.enums.Department;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamApplyForm {

    private List<String> memberIds;

    private Long eventId;

    private String teamName;

    private String applicant;

    private List<Department> excludeDepartments;

}
