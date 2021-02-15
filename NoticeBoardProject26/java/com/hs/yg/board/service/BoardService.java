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

/**
 * <pre>
 * BoardService
 * </pre>
 * 
 * @author wjddy
 */
@Service
public class BoardService {
	@Resource(name = "fileUtils")
	private FileUtils fileUtils;

	@Autowired
	BoardDao dao;

	/**
	 * <pre>
	 * 게시판 글 목록을 가져오는 매소드
	 * </pre>
	 * 
	 * @param vo 뷰로부터 가져온 Board 타입의 데이터
	 * @return 질의를 통해 가져온 데이터를 List<Board>의 형태로 반환함
	 * @throws SQLException
	 */
	public List<Board> getBoardList(Board vo) throws SQLException {
		return dao.getBoardList(vo);
	}

	/**
	 * <pre>
	 * 글의 데이터를 가져오는 메소드
	 * </pre>
	 * 
	 * @param vo 뷰로부터 가져온 Board 타입의 데이터
	 * @return 질의를 통해 가져온 데이터를 Board의 형태로 반환함
	 * @throws SQLException
	 */
	public Board getBoard(Board vo) throws SQLException {
		return dao.getBoard(vo);
	}

	/**
	 * <pre>
	 * getContent의 오버로딩
	 * </pre>
	 * 
	 * @param bno 게시판 번호
	 * @return 질의를 통해 가져온 데이터를 Board의 형태로 반환함
	 * @throws SQLException
	 */
	public Board getBoard(int bno) throws SQLException {
		return dao.getBoard(bno);
	}

	/**
	 * <pre>
	 * 글과 파일을 등록하는 메소드
	 * </pre>
	 * 
	 * @param vo        뷰로부터 가져온 Board 타입의 데이터
	 * @param mpRequest 파일 입출력을 사용하기 위한 MultipartHttpServletRequest 타입의 객체
	 * @return 질의를 통해 가져온 데이터들 중 게시판 번호를 반환함
	 * @throws Exception
	 */
	public int addBoard(Board vo, MultipartHttpServletRequest mpRequest) throws Exception {
		int bno = dao.addBoard(vo);
		List<Map<String, Object>> list = fileUtils.parseInsertFileInfo(bno, mpRequest);
		int size = list.size();
		for (int i = 0; i < size; i++) {
			dao.addFile(list.get(i));
		}
		return bno;
	}

	/**
	 * <pre>
	 * 글과 글이 가진 파일, 댓글을 삭제하는 메소드
	 * </pre>
	 * 
	 * @param vo 뷰로부터 가져온 Board 타입의 데이터
	 * @throws SQLException
	 */
	public void removeBoard(Board vo) throws SQLException {
		dao.removeBoard(vo);
	}

	/**
	 * <pre>
	 * 댓글을 입력하는 메소드
	 * </pre>
	 * 
	 * @param vo 뷰로부터 가져온 Reply 타입의 데이터
	 * @throws SQLException
	 */
	public void addReply(Reply vo) throws SQLException {
		dao.addReply(vo);
	}

	/**
	 * <pre>
	 * 해당 bno의 댓글 목록을 가져오는  메소드
	 * </pre>
	 * 
	 * @param bno 게시판 번호
	 * @return 질의를 통해 가져온 데이터를 List<Reply>의 형태로 반환함
	 * @throws SQLException
	 */
	public List<Reply> getReplyList(int bno) throws SQLException {
		return dao.getReplyList(bno);
	}

	/**
	 * <pre>
	 * 해당 조건에 해당되는 글 목록을 가져오는 메소드
	 * </pre>
	 * 
	 * @param searchOption 검색조건
	 * @param keyword      검색어
	 * @param displayPost  현재 사용하고 있는 페이지 넘버
	 * @param pageNum      표시할 페이지의 수
	 * @return 질의를 통해 가져온 데이터를 List<Board>의 형태로 반환함
	 * @throws SQLException
	 */
	public List<Board> getSearchBoardList(String searchOption, String keyword, int displayPost, int pageNum) throws SQLException {
		return dao.getSearchBoardList(searchOption, keyword, displayPost, pageNum);
	}

	/**
	 * <pre>
	 * 총 글의 개수를 가져오는 메소드
	 * </pre>
	 * 
	 * @return 글의 총 개수를 반환함
	 * @throws SQLException
	 */
	public int getBoardCnt() throws SQLException {
		return dao.getBoardCnt();
	}

	/**
	 * <pre>
	 * 검색 조건과 페이징 조건에 해당되는 글의 개수를 가져오는 메소드
	 * </pre>
	 * 
	 * @param searchOption 검색 조건
	 * @param keyword      검색어
	 * @return 검색 조건에 맞는 글의 총 개수를 반환함
	 * @throws SQLException
	 */
	public int getSearchBoardListCount(String searchOption, String keyword) throws SQLException {
		return dao.getSearchBoardListCount(searchOption, keyword);
	}

	/**
	 * <pre>
	 * 해당 페이징 조건의 글 목록을 가져오는 메소드
	 * </pre>
	 * 
	 * @param displayPost 현재 사용하고 있는 페이지 넘버
	 * @param pageNum     표시할 페이지의 수
	 * @return 질의를 통해 가져온 데이터를 List<Board>의 형태로 반환함
	 * @throws SQLException
	 */
	public List<Board> getBoardList(int displayPost, int pageNum) throws SQLException {
		return dao.getBoardList(displayPost, pageNum);
	}

	/**
	 * <pre>
	 * 해당 bno의 파일 목록을 가져오는 메소드
	 * </pre>
	 * 
	 * @param bno 게시판 번호
	 * @return 질의를 통해 가져온 데이터를 List<Map<String, Object>>의 형태로 반환함
	 * @throws SQLException
	 */
	public List<Map<String, Object>> getFileList(int bno) throws SQLException {
		return dao.getFileList(bno);
	}

	/**
	 * <pre>
	 * 해당 fno의 파일을 다운로드하는 메소드
	 * </pre>
	 * 
	 * @param fno 파일 번호
	 * @return 질의를 통해 가져온 데이터를 Map<String, Object>의 형태로 반환함
	 * @throws SQLException
	 */
	public Map<String, Object> getFile(int fno) throws SQLException {
		return dao.getFile(fno);
	}

	/**
	 * <pre>
	 * 작성자, 제목, 본문을 비롯해 파일 삭제 및 파일 업로드를 업데이트하는 메솓,
	 * </pre>
	 * 
	 * @param vo        뷰로부터 가져온 Board 타입의 데이터
	 * @param files     뷰로부터 가져온 String 배열 타입의 파읾 번호
	 * @param fileNames 뷰로부터 가져온 String 배열 타입의 파읾 번호
	 * @param mpRequest 파일 입출력을 사용하기 위한 MultipartHttpServletRequest 타입의 객체
	 * @throws Exception
	 */
	public void modifyBoard(Board vo, String[] files, String[] fileNames, MultipartHttpServletRequest mpRequest) throws Exception {
		dao.modifyBoard(vo);
		List<Map<String, Object>> list = fileUtils.parseUpdateFileInfo(vo, files, fileNames, mpRequest);
		Map<String, Object> tempMap = null;
		int size = list.size();
		for (int i = 0; i < size; i++) {
			tempMap = list.get(i);
			if (tempMap.get("IS_NEW").equals("Y")) {
				dao.addFile(tempMap);
			} else {
				dao.modifyFile(tempMap);
			}
		}
		dao.removeFile();
	}
}
