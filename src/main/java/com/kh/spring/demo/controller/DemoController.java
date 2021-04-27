package com.kh.spring.demo.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kh.spring.demo.model.service.DemoService;
import com.kh.spring.demo.model.vo.Dev;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/demo")
public class DemoController {
	
	//@Slf4j을 어노테이션으로 지정하면 자동으로 생성되는 코드
	//Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private DemoService demoService;
	// = new DemoServiceImpl(); 생략해도됨
	
	@RequestMapping("/devForm.do") // 사용자 요청 url에 따라 호출되는 메소드
	public String devForm() {
		return "demo/devForm";
	}
	
//	@RequestMapping("/demo/dev1.do") //GET POST 처리
	@RequestMapping(value = "/dev1.do", method = RequestMethod.POST)
	public String dev1(HttpServletRequest request, HttpServletResponse response, Model model) {
		String name = request.getParameter("name");
		int career = Integer.valueOf(request.getParameter("career"));
		String email = request.getParameter("email");
		String gender = request.getParameter("gender");
		String[] lang = request.getParameterValues("lang");
		
		Dev dev = new Dev(0, name, career, email, gender, lang);
		log.info("dev = {}", dev);
		
//		request.setAttribute("dev", dev);
		model.addAttribute("dev", dev); // model객체를 통해 jsp에 전달
		
		
		return "/demo/devResult";
	}
	
	@RequestMapping(value ="/dev2.do", method = RequestMethod.POST)
	public String dev2(
				@RequestParam(value="name") String name, 
				//  폼에 name값과 변수명이 같을때 자동으로 맵핑된다. 다를시에는 (value="")속성을 통해 수동으로 맵핑해줄수 있다.
				@RequestParam int career,
				@RequestParam String email,
				@RequestParam(defaultValue = "M") String gender,
				// defaultValue 속성은 사용자가 값을 입력하지 않았을시에 default값으로 "M"이라고 지정해둔 값이 들어간다.
				@RequestParam(required = false) String[] lang,
				// required 속성이 true일때 값이 없으면 에러페이지, false면 값이 없어도 정상작동한다.
				Model model
			) {
		Dev dev = new Dev(0, name, career, email, gender, lang);
		log.info("dev = {}", dev);
		
		model.addAttribute("dev", dev);
		return "/demo/devResult";
	}
	
	
	/**
	 * 커맨드 객체 : 사용자입력값과 일치하는 필드에 값대입
	 * 커맨드객체는 자동으로 model속성으로 지정
	 */
//	@RequestMapping(value ="/dev3.do", method = RequestMethod.POST)
	@PostMapping("/dev3.do")
	public String dev3(@ModelAttribute("ddev") Dev dev) { // 폼의 name값을 복사해 같은이름의 Dev 필드에있는 변수에 붙여넣기함
		log.info("{}", dev);
		return "/demo/devResult";
	}
	
	@PostMapping("/insertDev.do")
	public String insertDev(Dev dev, RedirectAttributes redirectAttr) {
		//1. 업무로직
		int result = demoService.insertDev(dev);
		//2. 사용자 피드백 및 redirect(DML)
		String msg = result > 0 ? "Dev등록 완료" : "Dev등록 실패";
		log.info("처리결과 {}", msg);
		//리다이렉트후 사용자피드백 전달
		redirectAttr.addFlashAttribute("msg", msg);
		
		
		return "redirect:/";
	}
	
	@GetMapping("/devList.do")
	public String devList(Model model) {
		List<Dev> list = demoService.selectDevList();
		log.info("list = {}", list);
		// jsp 위임
		model.addAttribute("list", list);
		return "/demo/devList";
	}
	
	@GetMapping("/updateDev.do")
	public String updateDev(Model model, @RequestParam int no) {
		Dev dev = demoService.selectOne(no);
		log.info("dev = {}", dev);
		List<String> langList = Arrays.asList(dev.getLang());
		model.addAttribute("langList", langList);
		model.addAttribute("dev", dev);
		return "/demo/devUpdateForm";
	}
	@PostMapping("/updateDev.do")
	public String updateDev(Dev dev, RedirectAttributes redircetAttr) {
		log.info("dev = {} ", dev);
		int result = demoService.updateDev(dev);
		String msg = result > 0 ? "수정성공" : "수정실패";
		redircetAttr.addFlashAttribute("msg", msg);
		return "redirect:/demo/devList.do";
	}
	@RequestMapping("/deleteDev.do")
	public String deleteDev(@RequestParam int no, RedirectAttributes redirectAttr) {
		int result = demoService.deleteDev(no);
		String msg = result > 0 ? "수정성공" : "수정실패";
		redirectAttr.addFlashAttribute("msg", msg);
		return "redirect:/demo/devList.do";
	}
}
