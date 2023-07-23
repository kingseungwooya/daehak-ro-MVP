package daehakro.server.domain.member.mail.dto.request;

import daehakro.server.domain.member.enums.Department;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerifyDto {
    private String uId;
    private String univName;
    private Department department;
    private String email;
}
