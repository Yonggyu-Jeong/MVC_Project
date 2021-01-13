<%@ page import="com.hs.yg.board.Board" %>
<%@ page import="com.hs.yg.board.Reply" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글 상세</title>
</head>
<body>
	<h1>글 상세</h1>
	<c:choose>
		<c:when test="${empty board.passwd}"> <!-- board.passwd가 없다면 content 컨트롤러로 bno를 별도의 입력없이 전달 후 board의 제목, 작성자, 내용을 출력 -->
		<form action="content" method="post">
		<input name="bno" type="hidden" value="${board.bno}" />
			<table border="1">
				<tr>
					<td bgcolor="orange">제목</td>
					<td align="left">${board.title }</td>
				</tr>
			<tr>
				<td bgcolor="orange">작성자</td>
				<td align="left">${board.name }</td>
			</tr>
			<tr>
				<td bgcolor="orange">내용</td>
				<td align="left">${board.content }</td>
			</tr>
			</table>
		</form>
			  <form action="writeReply" method="post"> <!-- board.passwd가 없다면 writeReply 컨트롤러에  별도의 입력이 없는 bno와 입력이 필요한 rwriter, rcontent를 전달 -->
    <input name="bno" type="hidden" value="${board.bno}" />
    <label for="writer">댓글 작성자</label><input type="text" id="rwriter" name="rwriter" />
    <br/>
    <label for="content">댓글 내용</label><input type="text" id="rcontent" name="rcontent" />
    <button type="submit">댓글 입력</button>
  </form>
  
  <form action="content" method="post">
  	<c:choose>
	<c:when test="${!empty reply}"> <!-- reply가 비어있지 않다면 content 컨트롤러에 reply의 rwiter와 rcontent를 출력 -->
	<table border="1">
	<c:forEach items="${reply }" var="reply">
		<tr>
		<td align="left">${reply.rwriter }</td>
		<td align="left">${reply.rcontent }</td>
		</tr>
	</c:forEach>
	</table>
	</c:when>
	</c:choose>		
	</form>
		</c:when>
		<c:when test="${not empty board.passwd}"> <!-- board.passwd가 있다면 secret 컨트롤러로 bno와 password를 각 각 num과 tpasswd의 이름으로 전달  -->
			<form action="secret" method="post">
			<input name="num" type="hidden" value="${board.bno}" />
			<input name="tpasswd" type="text"/>			
    		<button type="submit">비밀번호 입력</button>
			</form>					
		</c:when>
		</c:choose>	
<hr>
<a href="move">글 쓰기</a>&nbsp;&nbsp;&nbsp; <!-- 글 쓰기 하이퍼링크를 누른다면 move 컨트롤러로 이동 -->
<a href="delete?bno=${board.bno }">글 삭제</a>&nbsp;&nbsp;&nbsp;	<!-- 글 삭제 하이퍼링크를 누른다면 board.bno를  delete 컨트롤러에 전달 -->
<a href="listPage">글 목록</a>	  <!--  글 목록 하이퍼링크를 누른다면 listPage로 이동 -->
</body>
</html>