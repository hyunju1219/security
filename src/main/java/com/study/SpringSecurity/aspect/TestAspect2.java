package com.study.SpringSecurity.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(value = 1) //기본 execution 먼저 실행, order로 순서를 정할 수 있음
public class TestAspect2 {
    //aop 기본 틀, 생성
    @Pointcut("@annotation(com.study.SpringSecurity.aspect.annotation.Test2Aop)") //핵심 지점 지정
    private void pointCut() {}

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        CodeSignature signature = (CodeSignature) proceedingJoinPoint.getSignature(); //다운캐스팅하면 다양한 메서드 잇음
        Object[] args = proceedingJoinPoint.getArgs(); //매개변수 값(배열)
        String[] paramNames = signature.getParameterNames(); //매개변수명(배열로 리턴)
        for(int i = 0; i < args.length; i++) {
            System.out.println(paramNames[i] + ":" + args[i]);
        }
        System.out.println(signature.getName()); //메소드명
        System.out.println(signature.getDeclaringTypeName()); //메소드가 존재하는 클래스명(경로)
        System.out.println("전처리2"); //부가기능
        Object result = proceedingJoinPoint.proceed(); //핵심기능 호출할 때 aspect한 개 더 있음 -> 다른 aspect로 감(around의 return값 가져옴)
        System.out.println("test2" + result);
        System.out.println("후처리2");
        return result;
    }
}
