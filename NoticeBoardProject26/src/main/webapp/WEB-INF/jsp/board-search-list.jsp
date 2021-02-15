<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt-rt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%-- 프로젝트의 디렉토리 경로를 가져옴 --%>
<c:set var="path" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
<%-- jquery와 css인 bootstrap, content.css를 불러옴 --%>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="${path}/resources/css/bootstrap.css" rel="stylesheet">
<link href="${path}/resources/css/list.css" rel="stylesheet">
<title>검색한 게시글 목록 조회</title>
</head>
<body>
	<%-- 제목, 제목+내용, 내용을 선택할 수 있는 창과 검색  내용를 입력받아 컨트롤러인 searchListPage로 전달함 --%>
	<div class="br">
		<form action="getSearchBoardList" method="get">
			<%-- 라디오 버튼을 이용해 제목, 내용, 제목+내용 중 하나를 선택  --%>
			<select class="custom-select" name="searchOption" id="condition">
				<option value="title"
					<c:if test="${condition eq 'title' }">selected</c:if>>제목</option>
				<option value="context"
					<c:if test="${condition eq 'context' }">selected</c:if>>내용</option>
				<option value="titleCon"
					<c:if test="${condition eq 'titleCon' }">selected</c:if>>제목+내용</option>
			<%-- 키워드를 입력 --%>	
			</select> <input type="text" name="keyword" id="keyword" value="${keyword}"
				maxlength="12" />
			<button class="btn btn-primary btn-sm" type="submit">검색</button>
		</form>
	</div>
	<%-- 번호, 제목, 글쓴이, 조회수, 작성일자를 출력함 --%>
	<table class="table table-hover table-striped text-center listColor">
		<thead>
			<tr>
				<th class="number">번호</th>
				<th class="title">제목</th>
				<th class="writer">글쓴이</th>
				<th class="viewCount">조회수</th>
				<th class="regDate">작성일자</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${boardList }" var="board">
				<tr>
					<td class="c">${board.boardNum}</td>
					<%-- 제목을 누를 경우, 해당 제목의 bno를 컨트롤러 content로 넘겨줌, 제목은 오버 플로우를 적용 --%>					
					<td class="mainTitleLength left">
						<nobr>
							<a href="getBoard?bno=${board.bno}"> ${board.title}</a>
						</nobr>&nbsp; 
						 <%-- 해당 글의 댓글 수가 0이 아니면 표시 --%>		
						<span> 
							<c:if test="${board.commentCount ne 0}"> [${board.commentCount}]</c:if> 
						</span>
					</td>
					<td class="c">${board.name}</td>
					<td class="c">${board.viewCount}</td>
					<td class="c">${board.registerDate}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<hr>
	<div class="br">
		<%-- 컨트롤러 moveAddBoard로 이동 --%>	
		<button type="button" class="btn btn-outline-primary" onclick="location.href='moveAddBoard'">글 쓰기</button>
		<%-- 컨트롤러 listPage로 이동 --%>	
		<button type="button" class="btn btn-outline-primary" onclick="location.href='getBoardList'">검색 초기화</button>
	</div>
	<%-- 해당 페이지 번호를 입력 받으며, 페이지 번호 모두 하이퍼링크로 출력 --%>
	<div>
		<ul class="pagination">
			<%-- 페이지 번호가 1이 아닐 경우 fprev 활성화  --%>		
			<c:if test="${page.fprev}">
				<li class="page-item">
					<a class="page-link"href="getSearchBoardList?num=1&searchOption=${searchOption}&keyword=${keyword}"><span class="sr-only">First</span></a>
				</li>
			</c:if>
			
			<%--페이지의 끝이 아닐 경우 enext 활성화  --%>		
			<c:if test="${page.prev}">
				<li class="page-item">
					<a class="page-link" href="getSearchBoardList?num=${page.startPageNum - 1}&searchOption=${searchOption}&keyword=${keyword}"><span class="sr-only">Previous</span></a>
				</li>
			</c:if>
			
			<%-- 해당 페이지 번호를 입력 받으며, 지정한 페이지 번호 개수만큼 버튼 생성  --%>
			<c:forEach begin="${page.startPageNum}" end="${page.endPageNum}" var="num">
				<li class="page-item">
					<a class="page-link" href="getSearchBoardList?num=${num}&searchOption=${searchOption}&keyword=${keyword}">${num} </a></li>
			</c:forEach>
			
			<%--다음 페이지 그룹이 있을 경우 next 활성화  --%>		
			<c:if test="${page.next}">
				<li class="page-item">
					<a class="page-link" href="getSearchBoardList?num=${page.endPageNum + 1}&searchOption=${searchOption}&keyword=${keyword}"><span class="sr-only">Next</span></a>
				</li>
			</c:if>		
				
			<%-- 마지막 페이지 번호가 아닐 경우 fprev 활성화  --%>		
			<c:if test="${page.enext}">
				<li class="page-item">
					<a class="page-link" href="getSearchBoardList?num=${page.end}&searchOption=${searchOption}&keyword=${keyword}"><span class="sr-only">Last</span></a>
				</li>
			</c:if>
		</ul>
	</div>
</body>
</html>