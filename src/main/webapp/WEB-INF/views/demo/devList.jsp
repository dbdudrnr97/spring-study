<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<jsp:include page="/WEB-INF/views/common/header.jsp">
	<jsp:param value="Dev 목록" name="title"/>
</jsp:include>
<table class="table w-75 mx-auto">
    <tr>
      <th scope="col">번호</th>
      <th scope="col">이름</th>
      <th scope="col">경력</th>
      <th scope="col">이메일</th>
      <th scope="col">성별</th>
      <th scope="col">개발가능언어</th>
      <th scope="col">수정</th>
      <th scope="col">삭제</th>
    </tr>
    <c:forEach items="${list}" var="dev">
    <tr>
    	<td>${dev.no}</td>
    	<td>${dev.name}</td>
    	<td>${dev.career}</td>
    	<td>${dev.email}</td>
    	<td>${dev.gender}</td>
    	<td>
    		<c:forEach items="${dev.lang}" var="lang" varStatus="vs">
    			${lang}${vs.last ? '' : ','}
    		</c:forEach>
    	</td>
    	<td>
    		<button type="button" class="btn btn-success" onclick="updateDev(${dev.no})">수정</button>
    	</td>
    	<td>
    		<button type="button" class="btn btn-danger" onclick="deleteDev(${dev.no})">삭제</button>
    	</td>
    </tr>
    </c:forEach>
</table>
<form 
	name="devDelFrm"
	action="${pageContext.request.contextPath}/demo/deleteDev.do"
	method="POST">
	<input type="hidden" name="no" />
</form>
<script>
function updateDev(no){
	location.href = `${pageContext.request.contextPath}/demo/updateDev.do?no=\${no}`;
}
function deleteDev(no){
	if(confirm(`\${no}번 Dev정보를 삭제하시겠습니까?`)){
		var $frm = $(document.devDelFrm);
		$frm.find("[name=no]").val(no);
		$frm.submit();
	}
}
</script>
<jsp:include page="/WEB-INF/views/common/footer.jsp"></jsp:include>
