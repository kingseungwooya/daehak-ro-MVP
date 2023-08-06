package daehakro.server.domain.event.repo;

import daehakro.server.domain.event.EventLog;
import daehakro.server.domain.member.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventLogRepository extends JpaRepository<EventLog, Long> {
    boolean existsByEventIdAndMember(Long eventId, Member member);
    boolean existsByMemberAndIsClose(Member member, boolean isClose);

    EventLog findByMemberAndEventId(Member member, Long eventId);

    List<EventLog> findByEventId(Long eventId);

    List<EventLog> findByMember(Member member);
}
