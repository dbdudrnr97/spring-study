package com.kh.spring.common.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component // bean 등록 
@Aspect    // Aspect 선언
public class LoggerAspect {

	@Pointcut("execution(* com.kh.spring.memo..*(..))")
	public void pointcut() {}
	
	//AroundAdvice
	@Around("pointcut()")
	public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
//		Signature signature = joinPoint.getSignature();
//		//joinPoint 실행전 advice
//		
//		log.debug("[Before] {} " , signature);
		Object obj = joinPoint.proceed(); // 주업무 실행
//		//joinPoint 실행후 advice
//		log.debug("[After] {} " , signature);
		return obj;
	}
	
	//BeforeAdvice
	@Before("pointcut()")
	public void beforeAdvice(JoinPoint joinPoint) {
//		Signature signature = joinPoint.getSignature();
//		log.debug("[beforeAdvice] {} " , signature);
	}
}
