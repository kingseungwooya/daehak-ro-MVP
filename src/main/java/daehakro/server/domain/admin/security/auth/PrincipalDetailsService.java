package daehakro.server.domain.admin.security.auth;

import daehakro.server.domain.admin.security.Admin;
import daehakro.server.domain.admin.security.AdminRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService{

    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("PrincipalDetailsService : 진입");
        Admin admin = adminRepository.findByUsername(username);

        // session.setAttribute("loginUser", user);
        return new PrincipalDetails(admin);
    }
}