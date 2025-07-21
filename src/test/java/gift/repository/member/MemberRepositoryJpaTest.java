package gift.repository.member;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import gift.entity.Member;
import gift.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryJpaTest {
    @Autowired
    private MemberRepository memberRepositoryJpa;
    
    @Test
    void 저장() {
        Member member = new Member(null, "email@email.com", "pwpw", Role.USER);
        
        var actual = memberRepositoryJpa.save(member);
        
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getEmail()).isEqualTo(member.getEmail())
        );
    }
    
    @Test
    void 검색() {
        Member member = new Member(null, "email@email.com", "pwpw", Role.USER);
        memberRepositoryJpa.save(member);
        
        var actual = memberRepositoryJpa.findByEmail(member.getEmail()).get().getEmail();
        
        assertThat(actual).isEqualTo(member.getEmail());
    }
}