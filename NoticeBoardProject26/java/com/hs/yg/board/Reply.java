package com.hs.yg.board;

/**
 * ´ñ±Û °ü·Ã ¸ðµ¨
 * 
 * @author Á¤¿ë±Ô
 */
public class Reply {
	private int rbno;
	private int rrno;
	private String rcontent;
	private String rwriter;
	private String rRegDate;

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

	public String getrRegDate() {
		return rRegDate;
	}

	public void setrRegDate(String rRegDate) {
		this.rRegDate = rRegDate;
	}

}
