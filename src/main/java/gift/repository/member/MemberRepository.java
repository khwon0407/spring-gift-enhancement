package gift.repository.member;

import gift.entity.Member;

public interface MemberRepository {
    
    boolean existsByEmail(String email);
    
    Member registerMember(Member newMember);
    
    Member findMemberByEmail(String email);
    
    Member findMemberById(Long userId);
}
