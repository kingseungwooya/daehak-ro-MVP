package daehakro.server.domain.member.mail;

import daehakro.server.domain.member.mail.EmailToken;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailTokenRepository extends JpaRepository<EmailToken, String> {
    Optional<EmailToken> findByIdAndExpirationDateAfterAndExpired(String emailTokenId, LocalDateTime now, boolean expired);
}