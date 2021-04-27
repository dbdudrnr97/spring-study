package com.kh.spring.common;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HelloSpringUtils {

	/**
	 * 
	 * totalPage 전체페이지수     올림(totalContents / numPerPage)
	 * pageBarSize 페이지바의 페이지수 
	 * pageStart ~ pageEnd 페이지바 범위
	 * pageNo 증감변수
	 * 
	 * < 1 2 3 4 5 >  이전버튼 비활성화
	 * < 6 7 8 9 10 >
	 * < 11 12 >	    다음버튼 비활성화
	 * 
	 */
	public static String getPageBar(int totalContents, int cPage, int numPerPage, String url) {
		StringBuilder pageBar = new StringBuilder();
		
		int pageBarSize = 5;
		int totalPage = (int)Math.ceil((double)totalContents / numPerPage);
		// 나눈값을 올림처리후 정수형으로 바꿈
		
		///spring/board/boardList.do
		///spring/board/boardList.do?cPage=
		url = url + (url.indexOf("?") > -1 ? "&" : "?") + "cPage=";
		
		// 1 2 3 4 5 : pageStart 1 ~ pageEnd 5 
		// 6 7 8 9 10 : pageStart 6 ~ pageEnd 10 
		int pageStart = ((cPage - 1) / pageBarSize) * pageBarSize + 1;
		int pageEnd = pageStart + pageBarSize - 1;
		
		//증감변수
		int pageNo = pageStart;
		
		pageBar.append("<nav><ul class=\"pagination justify-content-center pagination-sm\">");
		//이전 영역
		if(pageNo == 1) {
			pageBar.append("<li class=\"page-item disabled\">\r\n"
					+ "      <a class=\"page-link\" href=\"#\" tabindex=\"-1\" aria-disabled=\"true\">Previous</a>\r\n"
					+ "    </li>");
		}
		else {
			pageBar.append("<li class=\"page-item\">\r\n"
					+ "      <a class=\"page-link\" href=\"javascript:paging("+ (pageNo -1) +")\" tabindex=\"-1\" aria-disabled=\"true\">Previous</a>\r\n"
					+ "    </li>");
		}
		
		//페이지 No 영역
		while(pageNo <= pageEnd && pageNo <= totalPage) { 
			//현재페이지인 경우, 링크비활성화
			if(pageNo == cPage) {
				pageBar.append("<li class=\"page-item active\"><a class=\"page-link\" href=\"#\">"+ pageNo +"</a></li>");
			}
			else {
				pageBar.append("<li class=\"page-item\"><a class=\"page-link\" href=\"javascript:paging("+pageNo+")\">"+ pageNo +"</a></li>");
			}
			pageNo++;
		}
		//다음 영역
		if(pageNo > totalPage) { // 마지막 페이지라면
			pageBar.append("<li class=\"page-item\">\r\n"
					+ "      <a class=\"page-link\" href=\"#\">Next</a>\r\n"
					+ "    </li>");
		}
		else {
			pageBar.append("<li class=\"page-item\">\r\n"
					+ "      <a class=\"page-link\" href=\"javascript:paging("+ pageNo +")\">Next</a>\r\n"
					+ "    </li>");
		}
		
		pageBar.append("</ul></nav>");
		pageBar.append("<script>function paging(pageNo){ location.href = '"+url+"' +pageNo; }</script>");
		return pageBar.toString();
	}

	



	public static File getRenamedFile(String saveDirectory, String oldName) {
		//파일명 새로 부여 20210216_135645123_123.jpg
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmssSSS_");
		DecimalFormat df = new DecimalFormat("000"); //30 -> 030
		
		//확장자가져오기
		String ext = "";
		int dot = oldName.lastIndexOf(".");
		if(dot > -1)
			ext = oldName.substring(dot);//.jpg
		
		//새로운 파일명
		String newName = 
				sdf.format(new Date()) + df.format(Math.random() * 999) + ext;
		
		//새로운 파일객체
		//java.io.File.File(String parent, String child)
		File newFile = new File(saveDirectory, newName);
		return newFile;
	}
}
