<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<jsp:include page="/WEB-INF/views/common/header.jsp">
	<jsp:param value="게시판" name="title"/>
</jsp:include>
<style>
/*글쓰기버튼*/
input#btn-add{float:right; margin: 0 0 15px;}
</style>
<script>
function goBoardForm(){
	location.href = "${pageContext.request.contextPath}/board/boardForm.do";
}
</script>
<section id="board-container" class="container">
	<input type="button" value="글쓰기" id="btn-add" class="btn btn-outline-success" onclick="goBoardForm();"/>
	<table id="tbl-board" class="table table-striped table-hover">
		<tr>
			<th>번호</th>
			<th>제목</th>
			<th>작성자</th>
			<th>작성일</th>
			<th>첨부파일</th> <!-- 첨부파일 있을 경우, /resources/images/file.png 표시 width: 16px-->
			<th>조회수</th>
		</tr>
		<c:forEach items="${list}" var="board">
		<tr>
			<th>${board.no}</th>
			<th>${board.title}</th>
			<th>${board.memberId }</th>
			<th><fmt:formatDate value="${board.regDate}" pattern="yyyy-MM-dd"/></th>
			<th>
				<c:if test="${board.attachCount gt 0}">
	            <img src="${pageContext.request.contextPath}/resources/images/file.png" alt="file" style="height: 20px"/>
	            </c:if>
			</th>
			<th>${board.readCount}</th>
		</tr>
		</c:forEach>
	</table>
	${pageBar}
</section> 
<script>
function goBoardForm(){
	location.href='${pageContext.request.contextPath}/board/boardForm.do';
}

</script>
<jsp:include page="/WEB-INF/views/common/footer.jsp"></jsp:include>
