<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String ctx = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>새글등록</title>
<script type="text/javascript" src="<%=ctx %>/resources/SE2/js/HuskyEZCreator.js" charset="utf-8"></script>
<script type="text/javascript" src="http://code.jquery.com/jquery-1.9.0.min.js"></script>
<script type="text/javascript">
var oEditors = [];
$(function(){
      nhn.husky.EZCreator.createInIFrame({
          oAppRef: oEditors,
          elPlaceHolder: "ir1",
          sSkinURI: "/smarteditorSample/SE2/SmartEditor2Skin.html",  
          htParams	 : {
              bUseToolbar : true,             
              bUseVerticalResizer : false,     
              bUseModeChanger : false,         
              fOnBeforeUnload : function(){
                   
              }
          }, 
          fOnAppLoad : function(){
              oEditors.getById["ir1"].exec("PASTE_HTML", [""]);
          },
          fCreator: "createSEditor2"
      });
      
      $("#save").click(function(){
          oEditors.getById["ir1"].exec("UPDATE_CONTENTS_FIELD", []);
          $("#frm").submit();
      });    
});
</script>
</head>

<!-- insert에 multipart/form-data(인코딩 x) 방식으로, 인코딩 없이 작성자, 비밀번호, 제목, 내용, 업로드의 name을 전달함 -->
<body>
	<form:form id = "frm" action="insert" method="POST" enctype="multipart/form-data" accept-charset="UTF-8">
		<table border="1">
		<tr>
			<td>작성자</td>
			<td align="left"><input type="text" name="name" size="10" style="width:250px" /></td>
		</tr>
		<tr>
            <td>비밀번호</td>
            <td align="left"><input type="password" name="passwd" size="10" style="width:250px"/>
        </tr>
        <tr>
            <td>제목</td>
            <td>
            	<input type="text" id="title" name="title" style="width:650px"/>
        </tr>
        <tr>
            <td>내용</td>
            <td>
                <textarea rows="10" cols="30" id="ir1" name="content" style="width:650px; height:350px; "></textarea>
            </td>
        </tr>
        <tr>
			<td width="70">업로드</td>
			<td align="left"><input type="file" name="file" multiple/></td>
		</tr>
			<tr>
            	<td colspan="2">
             	    <input type="button" id="save" value="저장"/>
             	    <button type="button" onclick="location.href='listPage'">취소하기</button>

            	</td>
        	</tr>
	</table>
</form:form>
</body>
</html>