package com.study.SpringSecurity.config;

import com.study.SpringSecurity.security.filter.JwtAccessTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//시큐리티 라이브러리는 필터들
//원래 WebSecurityConfigurerAdapter 추상클래스를 구현해서 쓰고있음 -> 우리가 만든 SecurityConfig를 적용시키겠다.
@EnableWebSecurity
@Configuration //클래스 안에서 @Bean 어노테이션 쓸 수 있음
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAccessTokenFilter jwtAccessTokenFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //configure 메서드가 실행되면 시큐리티 초기 세팅 -> HttpSecurity 객체 생성
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //super.configure(http); 부모의 메서드를 사용하지 않는다.
        http.formLogin().disable(); //기본 제공 로그인 창을 없앰
        http.httpBasic().disable(); //폼으로 만든 로그인 창을 없앰 -> 권한없음 (403)
        http.csrf().disable(); //위조 방지 스티커(토큰), ssr에서 사용

        //스프링 시큐리티가 세션을 생성하지도 않고 기존의 세션 사용 안함
        //http.sessionManagement().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 스프링 시큐리티가 세션을 생성하지 않겠다, 기존의 세션을 완전히 사용하지 않겠다라는 뜻은 아님
        // jwt 등의 토큰 인증 방식을 사용할 때 설정함
        http.cors(); //cross origin 씀(서로 다른 서버 연결)

        http.authorizeRequests()
                .antMatchers("/auth/**", "/h2-console/**") //주소 지정한 요청은(문자열로)
                .permitAll() //권한을 줌
                .anyRequest() //나머지 요청은
                .authenticated() //인증이 필요함(필터를 거쳐야함)
                .and()
                .headers()
                .frameOptions()
                .disable();

        //UsernamePasswordAuthenticationFilter필터가 authentication이 있는지 확인
        http.addFilterBefore(jwtAccessTokenFilter, UsernamePasswordAuthenticationFilter.class); //필터 순서, UsernamePasswordAuthenticationFilter 실행전에 필터 추가
    }
}
