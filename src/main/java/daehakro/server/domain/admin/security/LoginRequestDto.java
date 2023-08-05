package daehakro.server.domain.admin.security;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String username;
    private String password;
}