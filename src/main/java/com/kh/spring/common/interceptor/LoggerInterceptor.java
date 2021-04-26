package com.kh.spring.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import lombok.extern.slf4j.Slf4j;
/**
 * HandlerInterCepter
 * 1. handler 전처리 - preHandle => 로그인확인 인터셉터, 관리자권한 확인 인터셉터...
 * 2. handler 후처리 - postHandle (ModelAndView 참조가능)
 * 3. view 후처리 - afterCompletion
 */
@Slf4j
public class LoggerInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
//		log.debug("================================================================");
//		log.debug(request.getRequestURI());
//		log.debug("----------------------------------------------------------------");
		return super.preHandle(request, response, handler); // 항상 true 리턴
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView); // handler호출후 코드작성
		
//		log.debug("================================================================");
//		log.debug("mav = {}", modelAndView);
//		log.debug("------------------------------view------------------------------");
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		super.afterCompletion(request, response, handler, ex);
//		
//		log.debug("________________________________________________________________\n");

	}

}
