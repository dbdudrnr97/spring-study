package com.kh.spring.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class RunningTimeAspect {

	@Pointcut("execution(String com.kh.spring.memo.controller.MemoController.insertMemo(..))")
	public void pointcut() {}
	
	@Around("pointcut()")
	public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
		StopWatch stopwatch = new StopWatch();
		stopwatch.start();
		
		Object obj = joinPoint.proceed();
		
		stopwatch.stop();
		
		log.debug("stopwatch  = {}초", stopwatch.getTotalTimeSeconds());
		
		return obj;
	}
}
