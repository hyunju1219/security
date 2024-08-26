package com.study.SpringSecurity.service;

import com.study.SpringSecurity.Dto.request.ReqSigninDto;
import com.study.SpringSecurity.Dto.response.RespJwtDto;
import com.study.SpringSecurity.domain.entity.User;
import com.study.SpringSecurity.repository.UserRepository;
import com.study.SpringSecurity.security.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SigninService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    public RespJwtDto signin(ReqSigninDto dto) {
        //username을 찾으면 user 객체 리턴 아니면 예외 발생
        User user = userRepository.findByUsername(dto.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("사용자 정보를 다시 입력하세요")
        );
        //(평문, 암호문) 매칭시켜서 같으면 true
        if(!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("사용자 정보를 다시 입력하세요");
        }

        return RespJwtDto.builder()
                .accessToken(jwtProvider.generateUserToken(user))
                .build();
    }

}
