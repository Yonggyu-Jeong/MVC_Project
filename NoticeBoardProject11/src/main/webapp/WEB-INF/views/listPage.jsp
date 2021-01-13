<%@ page import="java.io.Console"%>
<%@ page import="java.util.List"%>
<%@ page import="com.hs.yg.board.dao.BoardDao"%>
<%@ page import="com.hs.yg.board.Board"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
	<center>
		<h1>글 목록</h1>
		<form action="searchListPage" method="get">
		<label for="searchOption">검색조건</label>
		<select name="searchOption" id="condition">
			<option value="title" <c:if test="${condition eq 'title' }">selected</c:if>>제목</option>
			<option value="context" <c:if test="${condition eq 'context' }">selected</c:if>>내용</option>
			<option value="titleCon" <c:if test="${condition eq 'titleCon' }">selected</c:if>>제목+내용</option>
		</select>
		<input type="text" name="keyword" id="keyword" value="${keyword}"/>
		<button type="submit">검색</button>
		</form>				
				
		<table border="1" cellpadding="0" cellspacing="0" width="700">
			<tr>
				<th bgcolor="orange" width="50">번호</th>
				<th bgcolor="orange" width="200">제목</th>
				<th bgcolor="orange" width="100">작성자</th>
				<th bgcolor="orange" width="50">조회수</th>
				<th bgcolor="orange" width="50">댓글수</th>
			</tr>
		<c:choose>
			<c:when test="${not empty boardList}">
				<c:forEach items="${boardList }" var="board">
					<tr>
						<td>${board.bno }</td>
						<td align="left"><a href="content?bno=${board.bno}">
							${board.title }</a></td>
						<td>${board.name }</td>
						<td>${board.viewCount }</td>
						<td>${board.commentCount }</td>
					</tr>
				</c:forEach>
			</c:when>		
			<c:otherwise>
				<tr>
					<td colspan="10">등록된 글이 없습니다.</td>
				</tr>
			</c:otherwise>
		</c:choose>
		</table>
	</center>
	<center>
	<div>
		<c:if test="${page.prev}">
		  <span>[ <a href="listPage?num=${page.startPageNum - 1}">이전</a> ]</span>
		</c:if>	
		<c:forEach begin="${page.startPageNum}" end="${page.endPageNum}" var="num">
		  <span><a href="listPage?num=${num}">${num}</a></span>
		</c:forEach>
		<c:if test="${page.next}">
		 <span>[ <a href="listPage?num=${page.endPageNum + 1}">다음</a> ]</span>
		</c:if>
	</div>
 	<a href="move">글 쓰기</a> 
	</center>


</body>
</html>