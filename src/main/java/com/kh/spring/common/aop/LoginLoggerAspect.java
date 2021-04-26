package com.kh.spring.common.aop;

import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.kh.spring.member.model.vo.Member;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Slf4j
@Component
public class LoginLoggerAspect {

	@Pointcut("execution(* com.kh.spring.member.controller..login(..))")
	public void pointcut() {}
	
	
	@AfterReturning(pointcut="pointcut()", returning = "returnObj")
	public void advice(JoinPoint joinPoint, Object returnObj) {
		log.debug("returnObj = {}", returnObj);
		
		ModelAndView mav = (ModelAndView) returnObj;
		Map<String, Object> model = mav.getModel();
		if(model.containsKey("loginMember")) {
			Member member = (Member)model.get("loginMember");
			log.info("{}[{}]님이 로그인했습니다.", member.getName(), member.getId());
		}
	}
}
