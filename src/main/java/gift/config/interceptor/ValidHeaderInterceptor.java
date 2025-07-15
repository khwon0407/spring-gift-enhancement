package gift.config.interceptor;

import gift.config.annotation.ValidHeader;
import gift.entity.Member;
import gift.entity.Role;
import gift.exception.common.HttpException;
import gift.exception.forbidden.WrongPermissionException;
import gift.exception.unauthorized.WrongHeaderException;
import gift.exception.unauthorized.WrongIdOrPasswordException;
import gift.repository.member.MemberRepositoryJpa;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ValidHeaderInterceptor implements HandlerInterceptor {
    
    private final MemberRepositoryJpa memberRepositoryJpa;
    
    public ValidHeaderInterceptor(MemberRepositoryJpa memberRepositoryJpa) {
        this.memberRepositoryJpa = memberRepositoryJpa;
    }
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws HttpException {
        
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        ValidHeader validHeader = handlerMethod.getMethodAnnotation(ValidHeader.class);
        
        if(validHeader == null) {
            return true;
        }
        
        String email = (String) request.getAttribute("email");
        Role tokenRole = (Role) request.getAttribute("role");
        
        Member member = memberRepositoryJpa.findByEmail(email).orElseThrow(
            WrongIdOrPasswordException::new);
        
        if(!member.getRole().equals(tokenRole)) {
            throw new WrongHeaderException();
        }
        
        request.setAttribute("member", member);
        
        Role requiredRole = validHeader.role();
        if (requiredRole != Role.NONE) {
            if (!tokenRole.equals(requiredRole)) {
                throw new WrongPermissionException();
            }
        }
        
        return true;
    }
}
