package com.kh.spring.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.FlashMapManager;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.kh.spring.member.model.vo.Member;

public class LoginInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession sesion = request.getSession();
		Member loginMember = (Member) sesion.getAttribute("loginMember");
		
		if(loginMember == null) {
			//RedirectAttributes 내부에서 사용하는 FlashMap에 사용자 피드백 저장(세션스코프에 저장)
			FlashMap flashMap = new FlashMap();
			flashMap.put("msg", "로그인후 이용하세요.");
			FlashMapManager manager = RequestContextUtils.getFlashMapManager(request);
			manager.saveOutputFlashMap(flashMap, request, response);
			
			
			response.sendRedirect(request.getContextPath()+"/member/login.do");
			return false;
		}
		
		return super.preHandle(request, response, handler);
	}
	

}
