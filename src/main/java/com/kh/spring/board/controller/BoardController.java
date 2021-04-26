package com.kh.spring.board.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kh.spring.board.model.service.BoardService;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.common.HelloSpringUtils;
import com.kh.spring.member.model.vo.Member;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/board")
public class BoardController {

	@Autowired
	private BoardService boardService;
	
	@GetMapping("/boardList.do")
	public void boardList(@RequestParam(defaultValue="1") int cPage, Model model, HttpServletRequest request) { // cPage = 현대페이지 default = 1
		//1. 사용자 입력값 처리
		int numPerPage = 5; // 한페이지당 개시글의 개수
		Map<String, Object> param = new HashMap<>();
		param.put("numPerPage", numPerPage);
		param.put("cPage", cPage);
		
		
		//2. 업무로직
		//a. contents 영역 : mybatis의 RowBounds
		List<Board> list = boardService.selectBoardList(param);
		
		//b. pageBar 영역
		int totalContents = boardService.getTotalContents();
		log.debug("totalContents {}", totalContents);
		String url = request.getRequestURI();
		log.debug("url = {}", url);
		String pageBar = HelloSpringUtils.getPageBar(totalContents, cPage, numPerPage, url);
		//3. jsp위임
		model.addAttribute("list", list);
		model.addAttribute("pageBar", pageBar);
	}
	
	@GetMapping("/boardForm.do")
	public void boardForm(HttpServletRequest request, Model model) {
		//로그인한 회원 확인
		Member member = (Member) request.getSession().getAttribute("loginMember");
		log.debug("member = {}" , member);
		
		model.addAttribute("loginMember", member);
	}
	
	@PostMapping("boardEnroll.do")
	public String boardEnroll(Board board, RedirectAttributes redirectAttr) {
		try {
			int result = boardService.boardEnroll(board);
			redirectAttr.addFlashAttribute("msg", "게시물 저장이 완료되었습니다.");
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return "redirect:/board/boardList.do";
	}
	
	
}
