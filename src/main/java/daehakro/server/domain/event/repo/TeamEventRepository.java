package daehakro.server.domain.event.repo;

import daehakro.server.domain.event.Event;
import daehakro.server.domain.event.team.TeamEvent;
import daehakro.server.domain.member.team.Team;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamEventRepository extends JpaRepository<TeamEvent, Long> {
    List<TeamEvent> findAllByOrderByCreateAtDesc();

    List<TeamEvent> findAllByEndDateGreaterThanEqualOrderByStartDate(LocalDate today);
}
