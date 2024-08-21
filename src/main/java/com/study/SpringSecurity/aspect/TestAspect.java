package com.study.SpringSecurity.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(value = 2)
public class TestAspect {
    //aop 기본 틀, 생성
    @Pointcut("execution(* com.study.SpringSecurity.service.TestService.aop*(..))") //핵심 지점 지정
    private void pointCut() {}

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("전처리"); //부가기능
        Object result = proceedingJoinPoint.proceed(); //핵심기능 호출
        //메소드가 void면 값을 대입할 수 없지만 aop 라이브러리는 리플렉션이 적용되어 (null safe) void면 자동으로 null을 대입한다.
        System.out.println("test" + result);
        System.out.println("후처리");
        return result;
    }
}
