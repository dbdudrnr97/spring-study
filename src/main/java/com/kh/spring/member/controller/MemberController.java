package com.kh.spring.member.controller;

import java.beans.PropertyEditor;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.kh.spring.member.model.service.MemberService;
import com.kh.spring.member.model.vo.Member;

import lombok.extern.slf4j.Slf4j;

/**
 * Model 
 * - View단에서 처리할 데이터 저장소. Map객체
 * 1. Model<<interface>>
 * 		- viewName 리턴
 * 		-addAttribute(key, value)
 * 2. ModelMap
 * 		- viewName 리턴
 * 		- addAttribute(key, value)
 * 3. ModelAndView
 *		- viewName(jsp위치, redirect location) 포함, ModelAndView객체 리턴
 *		- addObject(key, value)
 *
 * @ModelAttribute
 * 1. 메소드레벨
 * 		- 해당메소드의 리턴값을 model에 저장해서 모든 요청에 사용
 * 2. 메소드 매개변수
 * 		- model에 저장된 동일한 이름의 속성이 있는 경우 getter로 사용 가능
 * 		- 해당매개변수를 model의 속성으로 저장
 */
@Slf4j
@Controller
@RequestMapping("/member")
@SessionAttributes(value = {"loginMember"}) // String 배열로 여러개 등록가능 ex ) value = {"test1", "test2" ...}
public class MemberController {

	@Autowired
	private MemberService memberService;
	
	@Autowired
	private BCryptPasswordEncoder bcryptPasswordEncdoer;
	
	@ModelAttribute("common")
	public Map<String, Object> common(){
//		log.info("@ModelAttribute - common 실행");
		Map<String, Object> map = new HashMap<>();
		map.put("adminEmail", "admin@spring.kh.com");
		return map;
	}
	
	
	@GetMapping("/memberEnroll.do")
	public void memberEnroll() {
		//ViewTranslator에 의해서 요청 url에서 view단 jsp주소를 추론한다.
	}
	
	@PostMapping("/memberEnroll.do")
	public String memberEnroll(Member member, RedirectAttributes redirectAttr) {
		
		try {
			//0. password -> 암호화 처리
			String rawPassword = member.getPassword();
			String encodedPassword = bcryptPasswordEncdoer.encode(rawPassword);
			
			member.setPassword(encodedPassword);
			
			int result = memberService.insertMember(member);
			String msg = result > 0 ? "success" : "failed";
			redirectAttr.addFlashAttribute("msg", msg);
		} catch(Exception e) {
			//1. 로깅작업
			log.error(e.getMessage(), e);
			// spring container에 던질것
			throw e;
		}
		
		return "redirect:/";
	}
	
	/**
	 * 커맨드객체 이용시 사용자입력값(String)을 특정필드타입으로 변환할 editor객체를 설정
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		//Member.birthday: java.sql.Date타입 처리
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		//커스텀에디터 생성 : allowEmpty = true = 빈문자열을 null로 변환처리 허용
		PropertyEditor editor = new CustomDateEditor(sdf, true);
		binder.registerCustomEditor(java.sql.Date.class, editor);
		// sql.Date 를 변환할때 editor를 사용해라
	}
	
	@GetMapping("/login.do")
	public ModelAndView login(ModelAndView mav) {
		
		mav.addObject("test", "hello mav");
		mav.setViewName("member/login");
		
		return mav;
	}
	@PostMapping("/login.do")
	public ModelAndView login(@RequestParam String id, @RequestParam String password, 
					ModelAndView mav, HttpServletRequest request) {
		try{
			//1. 업무로직 : 해당 id의 member조회
			Member member = memberService.selectOneMember(id);
			log.info("encodedPassword = {} ", bcryptPasswordEncdoer.encode(password));
			
			//2. 로그인 여부처리
			if(member != null && bcryptPasswordEncdoer.matches(password, member.getPassword())) { // 로그인 성공
				//matches(사용자입력패스워드, db에 존재하는 패스워드)
				//기본값으로 request scope속성에 저장된다.
				//클래스레벨에 @SessionAttributes("loginMember") 지정하면, session scope에 저장
				mav.addObject("loginMember", member);				
			} else { // 로그인 실패
//				redirectAttr.addFlashAttribute("msg", "아이디 또는 비밀번호가 일치하지 않습니다.");
				FlashMap flashMap = RequestContextUtils.getOutputFlashMap(request);
				flashMap.put("msg", "아이디 또는 비밀번호가 일치하지 않습니다.");
			}
			
			//3. 사용자피드백 및 리다이렉트
			
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}
		
		mav.setViewName("redirect:/");
		return mav;
	}
	
	/**
	 * @SessionAttributes 를 통한 로그인은
	 * SessionStatus객체를 통해서 사용완료처리 함으로 로그아웃 한다.
	 */
	@GetMapping("/logout.do")
	public String logout(SessionStatus sessionStatus) {
		if(!sessionStatus.isComplete()) 
			sessionStatus.setComplete();
		
		return "redirect:/";
	}
	
	@GetMapping("/memberDetail.do")
	public ModelAndView memberDetail(@ModelAttribute("loginMember") Member loginMember, ModelAndView mav) {
		List<String> hobbyList = Arrays.asList(loginMember.getHobby());
		mav.addObject("hobbyList", hobbyList);
		mav.setViewName("member/memberDetail");
		return mav;
	}
	
	@PostMapping("/memberUpdate.do")
	public String memberUpdate(Member member, RedirectAttributes redirectAttr, Model model) {
		log.info("member = {}", member);
		try {
			int result = memberService.memberUpdate(member);
			String msg = result > 0 ? "수정 성공" : "수정 삭제";
			Member loginMember = memberService.selectOneMember(member.getId());
			model.addAttribute("loginMember", loginMember);
			
			redirectAttr.addFlashAttribute("msg", msg);

		} catch(Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}
		return "redirect:/member/memberDetail.do";
	}
}
