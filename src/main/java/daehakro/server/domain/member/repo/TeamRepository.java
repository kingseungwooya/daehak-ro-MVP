package daehakro.server.domain.member.repo;

import daehakro.server.domain.event.team.TeamEvent;
import daehakro.server.domain.member.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    void deleteAllByEvent(TeamEvent event);
}
