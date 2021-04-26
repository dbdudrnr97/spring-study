<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<jsp:include page="/WEB-INF/views/common/header.jsp">
	<jsp:param value="메모" name="title"/>
</jsp:include>

<style>
div#memo-container{width:60%; margin:0 auto;text-align:center;}
</style>
<div id="memo-container">
    <form name="memoFrm" action="${pageContext.request.contextPath}/memo/insertMemo.do" class="form-inline" method="post">
        <input type="text" class="form-control col-sm-6" name="memo" placeholder="메모" required/>&nbsp;
        <input type="password" class="form-control col-sm-2" name="password" maxlength="4" placeholder="비밀번호" required/>&nbsp;
        <button class="btn btn-outline-success" type="submit" >저장</button>
    </form>
    <br />
 
    <!-- 메모목록 -->
	<table class="table">
	    <tr>
	      <th>번호</th>
	      <th>메모</th>
	      <th>날짜</th>
	      <th>삭제</th>
	    </tr>
	    <c:forEach var="memo" items="${memoList}">
		    <tr>
		      <td>${memo.no}</td>
		      <td>${memo.memo}</td>
		      <td><fmt:formatDate value="${memo.regDate}" pattern="yyyy-MM-dd HH:mm"/></td>
		      <td><button type="button" class="btn btn-outline-danger">삭제</button></td>
			</tr>
	    </c:forEach>
	</table>
</div>
<script>
$(document.memoFrm).submit(e => {
	//패스워드 유효성 검사
	var $password = $(e.target).find("[name=password]");
	if(/^\d{4}$/.test($password.val()) === false){
		alert("비밀번호는 정수 4자리여야 합니다.");
		e.preventDefault(); // 폼 제출 방지
	}
	
});

</script>
<jsp:include page="/WEB-INF/views/common/footer.jsp"></jsp:include>
