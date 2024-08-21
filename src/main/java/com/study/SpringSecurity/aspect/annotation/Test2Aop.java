package com.study.SpringSecurity.aspect.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) //프로그램 실행될 때
@Target({ElementType.METHOD}) //어노테이션 적용 위치 : 메소드위에 적용 가능하다.
public @interface Test2Aop {}
