package com.kh.spring.board.model.vo;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Board {
	
	private int no;
	private String title;
	private String memberId;
	private String content;
	private Date regDate;
	private int readCount;
	
	private int attachCount; // 첨부파일 개수
	private List<Attachment> attachList; // 첨부파일 리스트
}
