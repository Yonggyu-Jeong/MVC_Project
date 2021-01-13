package com.hs.yg.board.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.hs.yg.board.Board;
import com.hs.yg.board.Reply;
import com.hs.yg.board.dao.BoardDao;
import com.hs.yg.util.FileUtils;

@Service
public class BoardService {
	@Resource(name="fileUtils")	
	private FileUtils fileUtils;
	
	@Autowired
	BoardDao dao;
	
	// 게시글 목록 가져오기 
	public List<Board> getBoardList(Board vo) throws SQLException {
		return dao.getBoardList(vo);
	}

	// 게시글 상세 보기
	public Board getContent(Board vo) throws SQLException {
		return dao.getContent(vo);
	}
	
	// 게시글 상세 보기 오버로딩
	public Board getContent(int bno) throws SQLException {
		return dao.getContent(bno);
	}

	// 글 쓰기
	public void insertBoard(Board vo, MultipartHttpServletRequest mpRequest) throws Exception {
		int bno = dao.insertBoard(vo);
		List<Map<String,Object>> list = fileUtils.parseInsertFileInfo(vo, mpRequest); 
		int size = list.size();
		for(int i=0; i<size; i++){ 
			dao.insertFile(list.get(i), bno); 
		}
	}
	
	// 글 삭제
	public void deleteBoard(Board vo) throws SQLException {
		dao.deleteBoard(vo);
	}
	
	// 댓글 쓰기
	public void insertReply(Reply vo) throws SQLException {
		dao.insertReply(vo);
	}
	
	// 댓글 목록 가져오기
	public List<Reply> getReplyList(int bno) throws SQLException {
		return dao.getReplyList(bno);
	}

	// 검색하기
	public List<Board> getSearchedBoardList(String searchOption, String keyword, int displayPost, int pageNum) throws SQLException {
		return dao.getSearchedBoardList(searchOption, keyword, displayPost, pageNum);
	}

	// 게시글 총 개수
	public int getBoardCnt() throws SQLException {
		return dao.getBoardCnt();
	}
	
	// 검색된 게시글 총 개수
	public int getSearchedBoardCnt(String searchOption, String keyword) throws SQLException {
		return dao.getSearchedBoardCnt(searchOption, keyword);
	}
	
	// 페이징
	public List<Board> getListPage(int displayPost, int pageNum) throws SQLException {
		 return dao.getListPage(displayPost, pageNum);
	}

}
