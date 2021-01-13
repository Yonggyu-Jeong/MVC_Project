package com.hs.yg.board.dao;

import org.springframework.stereotype.Repository;
import com.hs.yg.board.Board;
import com.hs.yg.board.Reply;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class BoardDao {	
	
	//DB의 정보
	final private String driver = "org.mariadb.jdbc.Driver"; 
	final private String url = "jdbc:mariadb://127.0.0.1:3306/board";
	final private String userid = "Jeoung"; 
	final private String userpw = "1111"; 
	
	//쿼리에 필요한 변수들
	private Connection conn; 
	private PreparedStatement pstmt = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	
	//insertBoard와 insertFile에 관련된 쿼리문
	final private String sqlI = "INSERT INTO dBoard(title, content, vname, passwd) values (?,?,?,?)";
	final private String sqlIF = "INSERT INTO dboard_file(bno, org_filename, new_filename, file_size) VALUES (?, ?, ?, ?)";
	
	//insertReply와 관련된 쿼리문
	final private String sqlRI = "INSERT INTO dboard_reply(bno, content, writer) VALUES (?, ?, ?)";
	final private String sqlRU = "UPDATE dboard SET replyCnt = (SELECT COUNT(*) FROM dboard_reply WHERE bno = ?) WHERE bno = ?";
	
	//deleteBoard와 관련된 쿼리문
	final private String sqlD = "DELETE FROM dboard WHERE bno=?";
	final private String sqlDR = "DELETE FROM dboard_reply WHERE bno=?";
	final private String sqlDF = "DELETE from dboard_file WHERE bno=?";
	
	//getContent에 관련된 쿼리문
	final private String sqlGCU = "UPDATE dboard SET viewCnt = (SELECT SUM(viewCnt) FROM dboard WHERE bno = ?) + 1 WHERE bno = ?"; 
	final private String sqlGC = "SELECT* FROM dboard where bno=?";
	
	//getBoardList에 관련된 쿼리문	
	final private String sqlGBL = "SELECT* FROM dboard";
	
	//getBoardCnt와 관련된 커리문 
	final private String sqlCnt = "select count(*) as cnt from dboard";	
	//getSearchedBoardCnt와 관련된 쿼리문
	final private String sqlSCntT = "select count(*) as cnt from dboard where title like ?";	
	final private String sqlSCntC = "select count(*) as cnt from dboard content like ?";	
	final private String sqlSCntTC = "select count(*) as cnt from dboard where title like ? or content like ?";	
	
	//getListPage와 관련된 쿼리문
	final private String sqlP = "select bno, title, content, vname, passwd, replyCnt, viewCnt from dboard order by bno desc LIMIT ?, ?";
	final private String sqlB = "select sum(file_size) as SUM from dboard_file where bno = ?";
	final private String sqlGB = "select bno from dboard order by bno desc LIMIT 0, 1";

	//getSearchedBoardList와 관련된 쿼리문
	final private String sqlST = "SELECT* FROM dboard where title like ? order by bno desc LIMIT ?, ?"; 
	final private String sqlSC = "SELECT* FROM dboard where content like ? order by bno desc LIMIT ?, ?"; 
	final private String sqlSTC = "SELECT* FROM dboard where title like ? or content like ? order by bno desc LIMIT ?, ?"; 
	
	//getReplyList와 관련된 쿼리문
	final private String sqlRGRL = "SELECT * FROM dboard_Reply where bno=?";	
	
	//DB 연결
	public void connect() {
		try { 
			Class.forName(driver); 
			conn = DriverManager.getConnection(url, userid, userpw); 
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	//DB 연결 해제
	public void disconnect() {
		try {
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//sqlI로 질의, 8859_1 형식의 데이터를 UTF-8로 전환 후, DB에 저장 -> sqlGB로 질의해 가장 최근의 게시글 1개의 bno를 가져와 반환함
	public int insertBoard(Board vo) throws SQLException {
		int bno = 0;
		connect();
		try {
			pstmt = conn.prepareStatement(sqlI);
			pstmt.setString(1, new String(vo.getTitle().getBytes("8859_1"), "UTF-8"));
			pstmt.setString(2, new String(vo.getContent().getBytes("8859_1"), "UTF-8"));
			pstmt.setString(3, new String(vo.getName().getBytes("8859_1"), "UTF-8"));
			pstmt.setString(4, new String(vo.getPasswd().getBytes("8859_1"), "UTF-8"));
			pstmt.executeUpdate();
			pstmt = conn.prepareStatement(sqlGB);
			rs = pstmt.executeQuery();
			if(rs.next())	bno = rs.getInt("bno");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		disconnect();
		return bno;
	}

	//sqlB로 질의해 해당 bno의 총 파일 용량을 구해 sum에 저장 -> sqlIF로 질의해 bno, 원래 파일 이름, 새로운 파일 이름, 파일 용량을 저장한다. 이때, 파일 용량이 100MB 이내일 때만 질의가 실행된다.
	public void insertFile(Map<String, Object> map, int bno) throws Exception{
		int sum = 0; 
		long longToInt = 0;
		String fileName = new String(map.get("ORG_FILE_NAME").toString().getBytes("8859_1"), "UTF-8");
		connect();
		pstmt = conn.prepareStatement(sqlB);
		pstmt.setInt(1, bno);	
		rs = pstmt.executeQuery();
		if(rs.next())	sum = rs.getInt("SUM");
		pstmt = conn.prepareStatement(sqlIF);
		pstmt.setInt(1, bno);			
		pstmt.setString(2, fileName);
		pstmt.setString(3, map.get("STORED_FILE_NAME").toString());
		longToInt = (Long) map.get("FILE_SIZE");
		pstmt.setLong(4, (Long) map.get("FILE_SIZE"));
		
		if(sum + (int)longToInt > 104857600) { // 파일의 용량 합이 100MB가 넘을 시, sql문을 실행하지 않음.
			return;
		}
		pstmt.executeUpdate();	
		disconnect();
	}
	
	//sqlDR를 통해 해당 bno를 가진 reply를 전부 삭제한다. sqlDF를 통해 해당 bno를 가진 File을 전부 삭제한다. 이후 sqlD를 통해 해당 bno를 가진 게시글을 삭제한다.
	public void deleteBoard(Board vo) throws SQLException {
		connect();
		pstmt = conn.prepareStatement(sqlDR);
		pstmt.setInt(1, vo.getBno());
		pstmt.executeUpdate();
		pstmt = conn.prepareStatement(sqlDF);
		pstmt.setInt(1, vo.getBno());
		pstmt.executeUpdate();
		pstmt = conn.prepareStatement(sqlD);
		pstmt.setInt(1, vo.getBno());
		pstmt.executeUpdate();
		disconnect();
	}

	//sqlGCU로 질의, 해당 게시글의 viewCnt를 1 증가 -> sqlGC를 통해 해당 bno의 bno, title, content, vname, passwd, replyCnt를 Board t에 저장한 후 반환한다.
	public Board getContent(Board vo) throws SQLException {
		Board t = null;
		connect();
		pstmt = conn.prepareStatement(sqlGCU);
		pstmt.setInt(1, vo.getBno());
		pstmt.setInt(2, vo.getBno());
		rs = pstmt.executeQuery();
		
		pstmt = conn.prepareStatement(sqlGC);
		pstmt.setInt(1, vo.getBno());
		rs = pstmt.executeQuery();
		if(rs.next()){
			t = new Board();
			t.setBno(rs.getInt("bno"));
			t.setTitle(rs.getString("title"));
			t.setContent(rs.getString("content"));
			t.setName(rs.getString("vname"));
			t.setPasswd(rs.getString("passwd"));
			t.setCommentCount(rs.getInt("replyCnt"));
		}
		disconnect();
		return t; 
	}
	
	public Board getContent(int dno) throws SQLException {
		Board t = null;
		connect();
		pstmt = conn.prepareStatement(sqlGCU);
		pstmt.setInt(1, dno);
		pstmt.setInt(2, dno);
		rs = pstmt.executeQuery();
		
		pstmt = conn.prepareStatement(sqlGC);
		pstmt.setInt(1, dno);
		rs = pstmt.executeQuery();
		if(rs.next()){
			t = new Board();
			t.setBno(rs.getInt("bno"));
			t.setTitle(rs.getString("title"));
			t.setContent(rs.getString("content"));
			t.setName(rs.getString("vname"));
			t.setPasswd(rs.getString("passwd"));
			t.setCommentCount(rs.getInt("replyCnt"));
		}
		disconnect();
		return t; 
	}
	
	//sqlGBL로 질의, bno, title, content, vname, passwd, replyCnt, viewCnt를 Board t에 저장한 후, List 형식으로 반환한다.
	public List<Board> getBoardList(Board vo) throws SQLException {
		List<Board> temp = new ArrayList<Board>();
		connect();
		stmt = conn.createStatement();
		rs = stmt.executeQuery(sqlGBL);
		while (rs.next()) { 
			Board t = new Board();
			t.setBno(rs.getInt("bno"));
			t.setTitle(rs.getString("title"));
			t.setContent(rs.getString("content"));
			t.setName(rs.getString("vname"));
			t.setPasswd(rs.getString("passwd"));
			t.setCommentCount(rs.getInt("replyCnt"));
			t.setViewCount(rs.getInt("viewCnt"));
			temp.add(t);
		}
		disconnect();
		return temp;
	}
	
	//sqlRI로 질의, 해당되는 bno에 Rcontent, Rwriter를  dboard_reply에 저장 -> sqlRu를 통해 해당되는 dboard의 bno에 replyCnt를 증가시킨다. 
	public void insertReply(Reply vo) throws SQLException {
		connect();
		pstmt = conn.prepareStatement(sqlRI); 	// 댓글을 데이터베이스 안에 저장한 후
		pstmt.setInt(1, vo.getRbno());
		pstmt.setString(2, vo.getRcontent());
		pstmt.setString(3, vo.getRwriter());
		pstmt.executeQuery();
			
		pstmt = conn.prepareStatement(sqlRU); 	// dboard의 replyCnt를 증가시킴
		pstmt.setInt(1, vo.getRbno());
		pstmt.setInt(2, vo.getRbno());
		pstmt.executeQuery();
		disconnect();
	}
	
	//sqlRGRL로 질의, 해당되는 bno의 rno, content, writer를 Reply t에 저장 후 List 형식으로 반환한다.
	public List<Reply> getReplyList(int bno) throws SQLException {
		List<Reply> temp = new ArrayList<Reply>();
		connect();	
		pstmt = conn.prepareStatement(sqlRGRL);
		pstmt.setInt(1, bno);
		rs = pstmt.executeQuery();
		while (rs.next()) { 
			Reply t = new Reply();
			t.setRbno(rs.getInt("bno"));
			t.setRrno(rs.getInt("rno"));
			t.setRcontent(rs.getString("content"));
			t.setRwriter(rs.getString("writer"));
			temp.add(t);
		}
		disconnect();
		return temp;
	}

	//searchOption 조건에 따라 sqlSTC, sqlST, sqlSC 이 셋 중 하나를 통해 질의한다. title, content, vname, passwd, replyCnt, viewCnt를 Board t에 저장한 후, List 형식으로 반환한다.
	public List<Board> getSearchedBoardList(String searchOption, String keyword, int displayPost, int pageNum) throws SQLException {
		List<Board> temp = new ArrayList<Board>();		
		String akeyword = "%"+keyword+"%";
		connect();
		if(searchOption.contentEquals("titleCon")) {	//조건에 따라  sql문을 다르게.
			pstmt = conn.prepareStatement(sqlSTC);
			pstmt.setString(1, akeyword);
			pstmt.setString(2, akeyword);
			pstmt.setInt(3, displayPost);
			pstmt.setInt(4, pageNum);
		}else if(searchOption.contentEquals("title")) {
			pstmt = conn.prepareStatement(sqlST);
			pstmt.setString(1, akeyword);
			pstmt.setInt(2, displayPost);
			pstmt.setInt(3, pageNum);
		}else {
			pstmt = conn.prepareStatement(sqlSC);
			pstmt.setString(1, akeyword);
			pstmt.setInt(2, displayPost);
			pstmt.setInt(3, pageNum);
		}
		rs = pstmt.executeQuery();	
		while (rs.next()) { 
			Board t = new Board();
			t.setBno(rs.getInt("bno"));
			t.setTitle(rs.getString("title"));
			t.setContent(rs.getString("content"));
			t.setName(rs.getString("vname"));
			t.setPasswd(rs.getString("passwd"));
			t.setCommentCount(rs.getInt("replyCnt"));
			t.setViewCount(rs.getInt("viewCnt"));
			temp.add(t);
		}
		disconnect();
		return temp;
	}

	//sqlCnt를 통해 질의, dboard의 count를 int로 받아 반환한다.
	public int getBoardCnt() throws SQLException {
		int temp = 0;
		connect();
		stmt = conn.createStatement();
		rs = stmt.executeQuery(sqlCnt);	
		if(rs.next())	temp = rs.getInt("cnt");	
		disconnect();
		return temp;
	}

	//searchOption 조건에 따라 sqlSCntTC, sqlSCntT, sqlSCntC 이 셋 중 하나를 통해 질의한다. 해당 조건의 게시글 수를 int로 받아 반환한다.
	public int getSearchedBoardCnt(String searchOption, String keyword) throws SQLException {
		int num = 0;
		String akeyword = "%"+keyword+"%";
		connect();
		if(searchOption.contentEquals("titleCon")) {
			pstmt = conn.prepareStatement(sqlSCntTC);
			pstmt.setString(1, akeyword);
			pstmt.setString(2, akeyword);
		}else if(searchOption.contentEquals("title")) {
			pstmt = conn.prepareStatement(sqlSCntT);
			pstmt.setString(1, akeyword);
		}else {
			pstmt = conn.prepareStatement(sqlSCntC);
			pstmt.setString(1, akeyword);
		}
		rs = pstmt.executeQuery();	
		if(rs.next())	num = rs.getInt("cnt");
		disconnect();
		return num;
	}
	
	public List<Board> getListPage(int displayPost, int postNum) throws SQLException {
		List<Board> temp = new ArrayList<Board>();
		connect();
		pstmt = conn.prepareStatement(sqlP);
		pstmt.setInt(1, displayPost);
		pstmt.setInt(2, postNum);
		rs = pstmt.executeQuery();	
		while (rs.next()) { 
			Board t = new Board();
			t.setBno(rs.getInt("bno"));
			t.setTitle(rs.getString("title"));
			t.setContent(rs.getString("content"));
			t.setName(rs.getString("vname"));
			t.setPasswd(rs.getString("passwd"));
			t.setCommentCount(rs.getInt("replyCnt"));
			t.setViewCount(rs.getInt("viewCnt"));
			temp.add(t);
		}
		disconnect();
		return temp;
		}
}
