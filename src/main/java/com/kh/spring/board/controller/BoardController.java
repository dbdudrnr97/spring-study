package com.kh.spring.board.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kh.spring.board.model.service.BoardService;
import com.kh.spring.board.model.vo.Attachment;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.common.HelloSpringUtils;

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
	public void boardForm() {
	}
	
	/**
		1. form[enctype=multipart/form-data]
		2. @RequestParam MultipartFile upFile
		3. 서버컴퓨터에 파일 저장 
				: saveDirectory (/resources/upload/board/20210427_12345_678.jpg)
				: 파일명 재지정
		4. DB[attachment]에 저장된 파일정보 등록
	 */
	@PostMapping("boardEnroll.do")
	public String boardEnroll(@ModelAttribute Board board,
								@RequestParam(value="upFile", required = false) MultipartFile[] upFiles,
								HttpServletRequest request,
								RedirectAttributes redirectAttr) {
		
		try {
			//0. 파일 저장
			//저장경로
			String saveDirectory = 
					request.getServletContext().getRealPath("/resources/upload/board");
			//위의 파일이 없다면 File객체를 통해서 directory생성 가능
			File dir = new File(saveDirectory);
			if(!dir.exists()) // dir이 존재하지 않는가?
				dir.mkdirs(); // 복수개 폴더 생성가능
			
			//복수개의 Attachment객체를 담을 list 생성
			List<Attachment> attachList = new ArrayList<Attachment>();

			
				for(MultipartFile upFile : upFiles) {
					if(upFile.isEmpty()) continue;
					log.debug("upFile = {}", upFile);
					log.debug("upFile = {}", upFile.getOriginalFilename());
					log.debug("upFile = {}", upFile.getSize());	
					//저장할 파일명 생성
					File renamedFile = HelloSpringUtils.getRenamedFile(saveDirectory, upFile.getOriginalFilename());
					//파일 저장
					upFile.transferTo(renamedFile);
					Attachment attach = new Attachment();
					attach.setOriginalFileName(upFile.getOriginalFilename());
					attach.setRenamedFileName(renamedFile.getName());
					attachList.add(attach);				
				}
			
			board.setAttachList(attachList);
			int result = boardService.insertBoard(board);
			
			
			redirectAttr.addFlashAttribute("msg", "게시물 저장이 완료되었습니다.");
			
		} catch (IOException | IllegalStateException e) {
			log.error("첨부파일 저장오류", e);
			throw new BoardException();
		} catch (Exception e) {
			log.error("게시물 등록 오류", e);
			throw e;
		}
		return "redirect:/board/boardList.do";
	}
	
	@GetMapping("/boardDetail.do")
	public String boardDetail(@RequestParam int no, Model model) {
		
		Board board = boardService.selectBoardDetail(no);
		model.addAttribute("board", board);
		return "/board/boardDetail";
	}
	
}
