package daehakro.server.domain.admin.security;

import daehakro.server.domain.admin.security.jwt.JwtAuthenticationFilter;
import daehakro.server.domain.admin.security.jwt.JwtAuthorizationFilter;
import daehakro.server.domain.admin.security.jwt.JwtConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;



@Configuration
@EnableWebSecurity // 시큐리티 활성화 -> 기본 스프링 필터체인에 등록
@EnableConfigurationProperties(JwtConfig.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    private final AdminRepository adminRepository;

    private final CorsConfig corsConfig;

    private final JwtConfig jwtConfig;

    public SecurityConfig(AdminRepository adminRepository, CorsConfig corsConfig, JwtConfig jwtConfig) {
        this.adminRepository = adminRepository;
        this.corsConfig = corsConfig;
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilter(corsConfig.corsFilter())
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtConfig))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), adminRepository, jwtConfig))
                .authorizeRequests()
                .antMatchers("/api/mvp/admin/**")
                .access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll();
    }
}
