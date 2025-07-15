package gift.service.member;

import gift.dto.api.member.LoginRequestDto;
import gift.dto.api.member.MemberRequestDto;
import gift.entity.Member;
import gift.entity.Role;
import gift.exception.conflict.AlreadyRegisteredException;
import gift.exception.unauthorized.WrongIdOrPasswordException;
import gift.repository.member.MemberRepositoryJpa;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberServiceImpl implements MemberService {
    private final MemberRepositoryJpa memberRepositoryJpa;
    
    public MemberServiceImpl(MemberRepositoryJpa memberRepositoryJpa) {
        this.memberRepositoryJpa = memberRepositoryJpa;
    }
    
    @Override
    @Transactional
    public LoginRequestDto registerMember(MemberRequestDto requestDto) {
        if (memberRepositoryJpa.existsByEmail(requestDto.email())) {
            throw new AlreadyRegisteredException();
        }
        
        Member newMember = new Member(null, requestDto.email(), requestDto.password(), Role.USER);
        
        Member registeredMember = memberRepositoryJpa.save(newMember);
        
        return new LoginRequestDto(registeredMember);
    }
    
    @Override
    public LoginRequestDto findMemberToLogin(MemberRequestDto requestDto) {
        Member member = memberRepositoryJpa.findByEmail(requestDto.email()).orElseThrow(
            WrongIdOrPasswordException::new);
        return new LoginRequestDto(member);
    }
}
