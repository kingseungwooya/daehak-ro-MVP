package daehakro.server.domain.member.repo;

import daehakro.server.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
    boolean existsByMemberIdAndKakaoKey(String memberId, String kakaoKey);
}
