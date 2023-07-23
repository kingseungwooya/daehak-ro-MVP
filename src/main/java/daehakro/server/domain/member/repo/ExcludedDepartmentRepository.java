package daehakro.server.domain.member.repo;

import daehakro.server.domain.member.ExcludedDepartment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExcludedDepartmentRepository extends JpaRepository<ExcludedDepartment, Long> {
    List<ExcludedDepartment> findAllByEventId(Long eventId);
    void deleteAllByEventId(Long eventId);
}
