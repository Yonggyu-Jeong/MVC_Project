package com.hs.yg.board;

import org.springframework.web.multipart.MultipartFile;

public class Board {
	private int bno;	//글의 번호	
	private String title;	//글의 제목
	private String name;	//글의 이름
	private String passwd;	//글의 비밀번호 (null 허용)
	private String content; //글의 내용
	private int commentCount; //글에 달린 댓글의 수
	private int viewCount; //글에 달린 댓글의 수
	private String fileName; //파일 입출력시_ 파일의 이름
	private MultipartFile uploadFile; //파일 입출력시_파일 객체
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
	public int getBno() {
		return bno;
	}
	public void setBno(int bno) {
		this.bno = bno;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public MultipartFile getUploadFile() {
		return uploadFile;
	}
	public void setUploadFile(MultipartFile uploadFile) {
		this.uploadFile = uploadFile;
	}
	public int getViewCount() {
		return viewCount;
	}
	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}
}
