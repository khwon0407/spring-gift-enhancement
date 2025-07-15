package gift.service.auth;

import gift.auth.JwtProvider;
import gift.dto.api.member.LoginRequestDto;
import gift.dto.api.member.MemberResponseDto;
import gift.entity.Member;
import gift.exception.unauthorized.WrongIdOrPasswordException;
import gift.repository.member.MemberRepositoryJpa;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    
    private final MemberRepositoryJpa memberRepositoryJpa;
    private final JwtProvider jwtProvider;
    
    public AuthServiceImpl(MemberRepositoryJpa memberRepositoryJpa, JwtProvider jwtProvider) {
        this.memberRepositoryJpa = memberRepositoryJpa;
        this.jwtProvider = jwtProvider;
    }
    
    @Override
    public MemberResponseDto login(LoginRequestDto requestDto) {
        Member member = memberRepositoryJpa.findByEmail(requestDto.email())
            .orElseThrow(WrongIdOrPasswordException::new);
        
        if(!member.getPassword().equals(requestDto.password())) {
            throw new WrongIdOrPasswordException();
        }
        
        String accessToken = jwtProvider.createToken(member);
        
        return new MemberResponseDto(accessToken);
    }
}
