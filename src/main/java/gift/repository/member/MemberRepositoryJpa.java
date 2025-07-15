package gift.repository.member;

import gift.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepositoryJpa extends JpaRepository<Member, Long> {

}
