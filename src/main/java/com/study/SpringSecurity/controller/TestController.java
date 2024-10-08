package com.study.SpringSecurity.controller;

import com.study.SpringSecurity.security.principal.PrincipalUser;
import com.study.SpringSecurity.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/test")
    public ResponseEntity<?> get() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalUser principalUser = (PrincipalUser) authentication.getPrincipal();
//        //aopTest()가 호출되기전 aspect의 around가 먼저 실행 -> around의 핵심기능 호출
//        System.out.println(testService.aopTest());
//        testService.aopTest2("박현주", 24);
//        testService.aopTest3("01012345678", "부산");

        return ResponseEntity.ok(principalUser);
    }
}
