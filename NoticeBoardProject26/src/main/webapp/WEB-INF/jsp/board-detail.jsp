<%@ page import="com.hs.yg.board.Board"%>
<%@ page import="com.hs.yg.board.Reply"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%-- 프로젝트의 디렉토리 경로를 가져옴 --%>
<c:set var="path" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<%-- jquery와 css인 bootstrap, content.css를 불러옴 --%>
<meta charset="UTF-8">
<script type="text/javascript" src="http://code.jquery.com/jquery-1.9.0.min.js"></script>
<link href="${path}/resources/css/bootstrap.css" rel="stylesheet">
<link href="${path}/resources/css/content.css" rel="stylesheet">
<title>글 상세</title>

<%-- 유효성 검사 기능  --%>
<script type="text/javascript">
	$(document).ready(function() {
		<%-- 페이지가 로딩된다면 class가 error로 지정된 메세지를 숨김처리함. --%>
		$('.error').hide();
		<%-- submit을 눌렀을 때, 해당 클래스를 가진 태그가 값이 없다면 해당 클래스의 error 태그를 표시, writer, content 순으로 검사 --%>
		$('.submit').click(function(event) {
			if ($('.insert_rwriter').val().length < 1) {
				$('.insert_rwriter').next().show()
				event.preventDefault();
			} else {
				$('.insert_rwriter').next().hide();
			}

			if ($('.insert_rcontent').val().length < 1) {
				$('.insert_rcontent').next().show()
				event.preventDefault();
			} else {
				$('.insert_rcontent').next().hide();
			}
		});
	});
</script>
</head>
<body>
	<div class="container" role="main">
		<c:choose>
	  		<%-- board의 passwd가 없다면 board의 내용을 표시함 --%>
	  		<c:when test="${empty board.passwd}">
				<table class="table table-striped contentTable">
		  			<thead>
						<tr> 
			  				<th colspan="3" class = "tableTh" id = "tColor">게시판 글 보기</th>
		    			</tr>
		 			</thead>
		 			<tbody>
						<tr>
			  				<td class="tp">작성자</td>
			  				<td colspan="2">${board.name }</td>
						</tr>
						<tr>
			  				<td>갱신일자</td>
			  				<td colspan="2">${board.modifyDate }</td>
						</tr>
						<tr>
			  				<td>제목</td>
			  				<td colspan="2">${board.title }</td>
						</tr>
						<tr>
			  				<td>내용</td>
			  				<td colspan="2" class="content"><div class="content">${board.content }</div></td>
						</tr>
		  			</tbody>
				</table>
				<hr>
				<form name="readForm" action="fileDown">
					<c:choose>
						<%-- file의 내용이 존재하면 파일 목록을 출력 --%>
						<c:when test="${!empty file}">
							<span>첨부 파일</span>
							<table border="1" class="rfTable">
								<c:forEach items="${file}" var="file">
									<tr>
										<%-- 파일 이름을 누르면, 하이퍼링크를 통해 컨트롤러 fileDown에 file_id를 넘겨줌--%>
										<td><a href="getFile?num=${file.file_id}">${file.org_filename}</a>(${file.file_size}kb)<br>
								    </tr>
								</c:forEach>
							</table>
							<hr>
						</c:when>
					</c:choose>
				</form>
				<form action="content" method="post">
					<c:choose>
					<%-- reply의 내용이 존재하면 댓글 목록을 출력--%>					
						<c:when test="${!empty reply}">
							<span>댓글 목록</span>
							<table border="1" class="rfTable">
								<c:forEach items="${reply }" var="reply">
									<tr>
										<td class="print_rwriter">
											<div>
												<span class="rwiter"></span>${reply.rwriter }
												<span class="date">${reply.rRegDate}</span>
											</div>
										</td>
									</tr>
									<tr>
										<td class="print_rcontent"><div><span class="label"></span>${reply.rcontent }</div></td>
									</tr>
								</c:forEach>
							</table>
							<hr>
						</c:when>
					</c:choose>
				</form>
				<%-- 댓글을 입력, 작성자와 내용을 모두 작성한 뒤 댓글쓰기를 누를 경우, 해당 정보와 board.bno를  컨트롤러 writeReply에 전송함 --%>
				<form name="reply" action="addReply" method="post">
					<input id="rbno" name="rbno" type="hidden" value="${board.bno}" />
					<table border="1" class="rfTable">
						<tr>
							<td>
								<div class="br">
									<textarea class="insert_rcontent form-control" placeholder="내용" class="insert_rcontent form-control" id="rcontent" name="rcontent" maxlength='250'></textarea>
									<span class="error">내용을 입력해주십시오.</span>													
								</div>
								<div class="bl">
									<input type="text" class="insert_rwriter form-control" placeholder="작성자" id="rwriter" name="rwriter" maxlength='14' />
<!--  								<input type="text" class="insert_rpasswd form-control" placeholder="비밀번호" id="rpassword" name="rpassword" maxlength='14' /> -->
									<span class="error">작성자 명을 입력해주십시오.</span>
								</div>
							</td>
						</tr>
						<tr>
							<td>
								<div>
									<button type="submit" class="submit btn btn-primary btn-lg br">댓글쓰기</button>
								</div>
							</td>
						</tr>
					</table>
				</form>
			</c:when>
		</c:choose>
		<c:choose>
			<%-- board.passwd가 존재할 경우, 비멀번호를 입력 후 컨트롤러 getSecretBoard에 전달 후 실행 --%>
			<c:when test="${not empty board.passwd}">
				<form action="getSecretBoard" method="post">
					<%-- 컨트롤러 getSecretBoard에 bno와 passwd를 전달 --%>
					<input name="bno" type="hidden" value="${board.bno}" /> <input name="tpasswd" type="text" />
					<button type="submit" class="btn btn-sm btn-primary">비밀번호 입력</button>
					<hr>
					<button type="button" class="btn btn-sm btn-primary" onclick="location.href='getBoardList'">취소</button>
				</form>
			</c:when>
			<%-- board.passwd가 없을 경우, 아래의 버튼들을 보여줌 --%>
			<c:when test="${empty board.passwd}">
				<div class="bs">
					<%-- 컨트롤러 moveAddBoard로 이동 --%>	
					<button type="button" class="btn btn-outline-primary" onclick="location.href='moveAddBoard'">글 쓰기</button> &nbsp;&nbsp;&nbsp;
					<%-- 컨트롤러 moveModifyBoard에 bno를 전달, 이동 --%>	
					<button type="button" class="btn btn-outline-primary" onclick="location.href='moveModifyBoard?bno=${board.bno }'">글 수정</button> &nbsp;&nbsp;&nbsp;
					<%-- 컨트롤러 delete에 board.bno에 bno를 전달, 이동 --%>	
					<button type="button" class="btn btn-outline-primary" onclick="location.href='removeBoard?bno=${board.bno }'">글 삭제</button> &nbsp;&nbsp;&nbsp;
					<%-- 컨트롤러 listPage로 이동 --%>	
					<button type="button" class="btn btn-outline-primary" onclick="location.href='getBoardList'">목록 가기</button>
				</div>
			</c:when>
		</c:choose>
	</div>
</body>
</html>