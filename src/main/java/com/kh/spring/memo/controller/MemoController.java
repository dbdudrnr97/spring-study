package com.kh.spring.memo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kh.spring.memo.model.service.MemoService;
import com.kh.spring.memo.model.vo.Memo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/memo")
public class MemoController {

	@Autowired
	private MemoService memoService;

	/**
	 * AOP의 실행 구조
	 * 자바 : MemoController.memo ---------------> MemoService.selectMemoList
	 * 스프링 : MemoController.memo ------Proxy---------> Target객체(MemoService.selectMemoList)
	 */
	
	@GetMapping("/memo.do")
	public void memo(Model model) {
		//proxy확인
		log.debug("porxy = {}", memoService.getClass());
		try {
			List<Memo> memoList = memoService.selectMemoList();
			model.addAttribute("memoList", memoList);			
		} catch (Exception e) {
			log.error("memo조회오류", e);
			throw e;
		}
	}
	
	@PostMapping("/insertMemo.do")
	public String insertMemo(@ModelAttribute Memo memo, RedirectAttributes redirectAttr) {
		try {
			int result = memoService.insertMemo(memo);
			redirectAttr.addFlashAttribute("msg", "메모등록 성공");
			
		} catch(Exception e) {
			log.error("memo등록 오류", e);
			throw e;
		}
		
		
		return "redirect:/memo/memo.do";
	}
}
