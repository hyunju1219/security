package com.study.SpringSecurity.security.jwt;

import com.study.SpringSecurity.domain.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    //상수 초기화 필요
    private final Key key;

    //yml 파일에서 secret을 문자열로 가져옴 , value는 autowide랑 비슷, 시작할 때 값 가져옴
    //객체 생성할 때 (컴포넌트기 때문에 IOC에 등록하려면)
    public JwtProvider(@Value("${jwt.secret}") String secret) {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)); //문자열을 decode해서 키형태로 넣음
    }

    public String removeBearer(String token) {
        return token.substring("Bearer ".length());
    }

    //세션 / 토큰
    //세션 : 사용자 정보를 세션에 저장하고 세션아이디만 넘겨줌(서버가 저장)
    //토큰 : 사용자 정보르 암호로 암호화해서 토큰 생성, 사용자아이디만, 민감한정보 들어있으면 안됨 => 정보가 필요하면 토큰 가지고 요청보내야함(클라이언트가 저장)
    //토큰 생성 메서드(사용자 정보를 바탕으로)
    public String generateUserToken(User user) {

        Date expireDate = new Date(new Date().getTime() + (1000l * 60 * 60 * 24 * 30)); //오늘 날짜를 다시 Date객체에 넣으면 Date객체로 반환, 30일

        String token = Jwts.builder()
                .claim("userId", user.getId()) //key-value형태로, 토큰에서 사용할 정보
                .expiration(expireDate) //유효기간 지정
                .signWith(key, SignatureAlgorithm.HS256) //서명, JwtProvider에 있는 key로 암호화(key, 암호화 알고리즘)
                .compact(); //빌더패턴

        return token;
    }

    //토큰 복호화 메서드
    public Claims parseToken(String token) {
        JwtParser jwtParser =  Jwts.parser()
                .setSigningKey(key)
                .build();

        //만료시간, 위조, kek값 변경 => 예외처리 필요
        return jwtParser.parseClaimsJws(token).getPayload(); // Claims객체를 리턴
    }
}
