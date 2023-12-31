package daehakro.server.domain.event.repo;

import daehakro.server.domain.event.Event;
import daehakro.server.domain.event.team.TeamEvent;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByOrderByCreateAtDesc();
    List<Event> findAllByEndDateGreaterThanEqualOrderByStartDate(LocalDate today);

}
