<%@ page import="java.io.Console"%>
<%@ page import="java.util.List"%>
<%@ page import="com.hs.yg.board.dao.BoardDao"%>
<%@ page import="com.hs.yg.board.Board"%>
<%@ page import="com.hs.yg.board.controller.BoardController"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>글 목록</title>
</head>
<body>
	<h1>글 상세</h1>
		<form action="secret" method="post">
		<input name="bno" type="hidden" value="${sboard.bno}" />
			<table border="1">
				<tr>
					<td bgcolor="orange">제목</td>
					<td align="left">${sboard.title }</td>
				</tr>
			<tr>
				<td bgcolor="orange">작성자</td>
				<td align="left">${sboard.name }</td>
			</tr>
			<tr>
				<td bgcolor="orange">내용</td>
				<td align="left">${sboard.content }</td>
			</tr>
		</table>
		</form>				
		  <form action="writeReply" method="post">
    <input name="bno" type="hidden" value="${sboard.bno}" />
    <label for="writer">댓글 작성자</label><input type="text" id="rwriter" name="rwriter" />
    <br/>
    <label for="content">댓글 내용</label><input type="text" id="rcontent" name="rcontent" />
    <button type="submit">댓글 입력</button>
  </form>

  <form action="secret" method="post">
  	<c:choose>
	<c:when test="${!empty sreply}">
	<table border="1">
	<c:forEach items="${sreply }" var="reply">
		<tr>
		<td align="left">${reply.rwriter }</td>
		<td align="left">${reply.rcontent }</td>
		</tr>
	</c:forEach>
	</table>
	</c:when>
	</c:choose>
				
</form>
<hr>
<a href="move">글 쓰기</a>&nbsp;&nbsp;&nbsp; 
<a href="delete?bno=${sboard.bno }">글 삭제</a>&nbsp;&nbsp;&nbsp;
<a href="listPage">글 목록</a>
</body>
</html>