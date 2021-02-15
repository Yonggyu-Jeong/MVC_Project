package com.hs.yg.board;

import org.springframework.stereotype.Component;

@Component
public class DataBase {
	private String driver;
	private String url;
	private String userid;
	private String userpw;

	DataBase() {
		driver = "org.mariadb.jdbc.Driver";
		url = "jdbc:mariadb://127.0.0.1:3306/board";
		userid = "Jeoung";
		userpw = "1111";
	}

	DataBase(String driver, String url, String userid, String userpw) {
		this.driver = driver;
		this.url = url;
		this.userid = userid;
		this.userpw = userpw;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUserpw() {
		return userpw;
	}

	public void setUserpw(String userpw) {
		this.userpw = userpw;
	}
}
