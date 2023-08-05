package daehakro.server.domain.event.repo;

import daehakro.server.domain.event.Couple;
import daehakro.server.domain.event.enums.EventType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoupleRepository extends JpaRepository<Couple, Long> {
    List<Couple> findAllByEventIdAndEventType(Long eventId, EventType eventType);
}
