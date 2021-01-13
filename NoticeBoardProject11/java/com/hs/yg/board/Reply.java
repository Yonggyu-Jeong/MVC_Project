package com.hs.yg.board;

public class Reply {
	private int rbno; // Board의 bno, 외래키이자 기본키
	private int rrno; // 댓글 번호
	private String rcontent; // 댓글의 내용
	private String rwriter; // 댓글 작성자
	public int getRbno() {
		return rbno;
	}
	public void setRbno(int rbno) {
		this.rbno = rbno;
	}
	public int getRrno() {
		return rrno;
	}
	public void setRrno(int rrno) {
		this.rrno = rrno;
	}
	public String getRcontent() {
		return rcontent;
	}
	public void setRcontent(String rcontent) {
		this.rcontent = rcontent;
	}
	public String getRwriter() {
		return rwriter;
	}
	public void setRwriter(String rwriter) {
		this.rwriter = rwriter;
	}

}
