package daehakro.server.domain.member.repo;

import daehakro.server.domain.member.UnivInfo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UnivRepository extends JpaRepository<UnivInfo, Long> {
    List<UnivInfo> findAll();
}

