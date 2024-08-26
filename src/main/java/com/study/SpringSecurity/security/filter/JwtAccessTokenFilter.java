package com.study.SpringSecurity.security.filter;

import com.study.SpringSecurity.domain.entity.User;
import com.study.SpringSecurity.repository.UserRepository;
import com.study.SpringSecurity.security.jwt.JwtProvider;
import com.study.SpringSecurity.security.principal.PrincipalUser;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAccessTokenFilter extends GenericFilter {

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        //요청의 헤더에서 키값으로 토큰을 꺼내온다
        String bearerAccessToken = request.getHeader("Authorization");

        if (bearerAccessToken != null) {
            String accessToken = jwtProvider.removeBearer(bearerAccessToken);
            Claims claims = null;
            try {
                claims = jwtProvider.parseToken(accessToken);
            } catch (Exception e) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
            Long userId = ((Integer) claims.get("userId")).longValue(); //Object로 반환

            //authentication 객체 생성 과정
            Optional<User> optionalUser = userRepository.findById(userId);
            if(optionalUser.isEmpty()) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }

            PrincipalUser principalUser = optionalUser.get().toPrincipalUser();
            //authentication 객체(전역): 응답되어 나가면 없어짐, 생명주기 : 요청 ~ 응답 => 요청마다 토큰이 필요함
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalUser, principalUser.getPassword(), principalUser.getAuthorities());
            System.out.println("예외 발생하지 않음");
            //스프링 시큐리티는 SecurityContextHolder의 context안에 Authentication 객체가 존재해야 인증이 되었다고 판단 -> 컨트롤러로 갈 수 있음
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(servletRequest, servletResponse); //다음으로 감
    }
}
