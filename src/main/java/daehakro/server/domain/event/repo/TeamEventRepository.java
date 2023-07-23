package daehakro.server.domain.event.repo;

import daehakro.server.domain.event.Event;
import daehakro.server.domain.event.team.TeamEvent;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamEventRepository extends JpaRepository<TeamEvent, Long> {
    List<TeamEvent> findAllByOrderByCreateAtDesc();
}
