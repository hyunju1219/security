package com.study.SpringSecurity.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ParamsPrintAspect {

    @Pointcut("@annotation(com.study.SpringSecurity.aspect.annotation.ParamsAop)")
    private void pointCut() {}

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object[] args = proceedingJoinPoint.getArgs();
        CodeSignature signature = (CodeSignature) proceedingJoinPoint.getSignature();
        Object result = proceedingJoinPoint.proceed();
        return result;
    }
}
