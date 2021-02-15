package com.hs.yg.board.controller;

import java.io.File;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hs.yg.board.Board;
import com.hs.yg.board.Page;
import com.hs.yg.board.Reply;
import com.hs.yg.board.service.BoardService;

/**
 * <pre>
 * BoardController
 * </pre>
 * 
 * @author 정용규
 */
@Controller
public class BoardController {
	@Autowired
	BoardService service;

	@RequestMapping("/")
	public String moveIndex() {
		return "board-index";
	}

	/**
	 * <pre>
	 * 게시판 글 목록을 가져오는 컨트롤러 메소드
	 * </pre>
	 * 
	 * @param model addAttribute를 사용하기 위한 Model 타입의 객체
	 * @param num   페이지 번호 값
	 * @throws Exception
	 */
	@RequestMapping("getBoardList")
	public String getBoardListController(Model model, @RequestParam(value = "num", defaultValue = "1") int num) throws Exception {
		Page page = new Page();
		page.setNum(num);
		page.setCount(service.getBoardCnt());
		List<Board> lPage = service.getBoardList(page.getDisplayPost(), page.getPostNum());
		model.addAttribute("boardList", lPage);
		model.addAttribute("page", page);
		model.addAttribute("select", num);
		return "board-list";
	}

	/**
	 * <pre>
	 * 검색 조건에 맞는 게시글 목록을 가져오는 컨트롤러 메소드
	 * </pre>
	 * 
	 * @param searchOption 검색 키워드
	 * @param keyword      검색어
	 * @param num          페이지 번호 값
	 * @param model        addAttribute를 사용하기 위한 Model 타입의 객체
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping(value = "getSearchBoardList")
	public String getSearchBoardListController(@RequestParam(value = "searchOption", required = false, defaultValue = "title") String searchOption,
			@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword, @RequestParam(value = "num", defaultValue = "1") int num, Model model)
			throws SQLException {
		Page page = new Page();
		page.setNum(num);
		page.setCount(service.getSearchBoardListCount(searchOption, keyword));
		List<Board> sLPage = service.getSearchBoardList(searchOption, keyword, page.getDisplayPost(), page.getPostNum());
		page.setSearchTypeKeyword(searchOption, keyword);
		model.addAttribute("boardList", sLPage);
		model.addAttribute("page", page);
		model.addAttribute("select", num);
		model.addAttribute("searchOption", searchOption);
		model.addAttribute("keyword", keyword);
		return "board-search-list";
	}

	/**
	 * <pre>
	 * 글과 글의 번호에 해당되는 댓글 그리고 파일의 내용을 가져오는 컨트롤러 메소드
	 * </pre>
	 * 
	 * @param vo    뷰로부터 가져온 Board타입의 데이터
	 * @param model addAttribute를 사용하기 위한 Model 타입의 객체
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping("getBoard")
	public String getBoardController(Board vo, Model model) throws SQLException {
		Board board = service.getBoard(vo);
		List<Reply> replyList = service.getReplyList(vo.getBno());
		List<Map<String, Object>> fileList = service.getFileList(vo.getBno());
		model.addAttribute("board", board);
		model.addAttribute("reply", replyList);
		model.addAttribute("file", fileList);
		return "board-detail";
	}

	/**
	 * <pre>
	 * 비밀글의 비밀번호 확인 및 비밀글의 내용을 가져오는 컨트롤러 메소드
	 * </pre>
	 * 
	 * @param bno    뷰로부터 가져온 게시판 번호
	 * @param passwd 뷰로부터 가져온 게시판 비밀번호
	 * @param model  addAttribute를 사용하기 위한 Model 타입의 객체
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping("getSecretBoard")
	public String getSecretBoardController(@RequestParam("bno") int bno, @RequestParam("tpasswd") String passwd, Model model) throws SQLException {
		Board board = service.getBoard(bno);
		String temp = "";
		List<Reply> replyList = service.getReplyList(bno);
		List<Map<String, Object>> fileList = service.getFileList(bno);
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			digest.reset();
			digest.update(passwd.getBytes("utf8"));
			temp = String.format("%064x", new BigInteger(1, digest.digest()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (board.getPasswd().equals(temp)) {
			model.addAttribute("sboard", board);
			model.addAttribute("sreply", replyList);
			model.addAttribute("file", fileList);
			model.addAttribute("passwd", passwd);
			return "board-secret-detail";
		} else {
			return "redirect:getBoardList";
		}
	}

	/**
	 * <pre>
	 * 글을 입력하는 컨트롤러 메소드
	 * </pre>
	 * 
	 * @param vo        뷰로부터 가져온 Board 타입의 데이터
	 * @param rttr      리다이렉트를 할 뷰에 데이터를 넘겨주기 위한 RedirectAttributes 타입의 객체
	 * @param mpRequest 파일 입출력을 사용하기 위한 MultipartHttpServletRequest 타입의 객체
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/addBoard", method = RequestMethod.POST)
	public String addBoardController(Board vo, RedirectAttributes rttr, MultipartHttpServletRequest mpRequest) throws Exception {
		int bno = service.addBoard(vo, mpRequest);
		rttr.addAttribute("bno", bno);
		return "redirect:getBoard";
	}

	/**
	 * <pre>
	 * insertBoard.jsp로 이동하기 위한 컨트롤러 메소드
	 * </pre>
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("moveAddBoard")
	public String moveAddBoardController() throws Exception {
		return "board-insert";
	}

	/**
	 * <pre>
	 * 글과 글에 관련된 댓글 및 파일 데이터를 삭제하는 컨트롤러 메소드
	 * </pre>
	 * 
	 * @param vo 뷰로부터 가져온 Board 타입의 데이터
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping("/removeBoard")
	public String removeBoardController(Board vo) throws SQLException {
		service.removeBoard(vo);
		return "redirect:getBoardList";
	}

	/**
	 * <pre>
	 * updateBoard.jsp로 이동하기 위한 컨트롤러 메소드
	 * </pre>
	 * 
	 * @param bno   뷰로부터 가져온 게시판 번호
	 * @param model addAttribute를 사용하기 위한 Model 타입의 객체
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping("moveModifyBoard")
	public String moveModifyBoardController(int bno, Model model) throws SQLException {
		Board board = service.getBoard(bno);
		List<Reply> replyList = service.getReplyList(bno);
		List<Map<String, Object>> fileList = service.getFileList(bno);
		model.addAttribute("board", board);
		model.addAttribute("reply", replyList);
		model.addAttribute("file", fileList);
		return "board-update";
	}

	/**
	 * <pre>
	 * 글의 내용을 수정하는 컨트롤러 메소드
	 * </pre>
	 * 
	 * @param vo        뷰로부터 가져온 Board 타입의 데이터
	 * @param rttr      리다이렉트를 할 뷰에 데이터를 넘겨주기 위한 RedirectAttributes 타입의 객체
	 * @param files     삭제할 파일 번호들을 String 배열 형태로 가져옴
	 * @param fileNames 삭제할 파일 이름들을 String 배열 형태로 가져옴
	 * @param mpRequest 파일 입출력을 사용하기 위한 MultipartHttpServletRequest 타입의 객체
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/modifyBoard", method = RequestMethod.POST)
	public String modifyBoardController(Board vo, RedirectAttributes rttr, @RequestParam(required = false, value = "fileNoDel[]") String[] files,
			@RequestParam(required = false, value = "fileNameDel[]") String[] fileNames, MultipartHttpServletRequest mpRequest) throws Exception {
		service.modifyBoard(vo, files, fileNames, mpRequest);
		rttr.addAttribute("bno", vo.getBno());
		return "redirect:getBoard";
	}

	/**
	 * <pre>
	 * 댓글을 등록하는 컨트롤러 메소드
	 * </pre>
	 * 
	 * @param vo   뷰로부터 가져온 Reply 타입의 데이터
	 * @param rttr 리다이렉트를 할 뷰에 데이터를 넘겨주기 위한 RedirectAttributes 타입의 객체
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping("/addReply")
	public String addReplyController(Reply vo, RedirectAttributes rttr) throws SQLException {
		service.addReply(vo);
		rttr.addAttribute("bno", vo.getRbno());

		return "redirect:getBoard";
	}

	/**
	 * <pre>
	 * 파일을 다운로드하기 위한 컨트롤러 메소드
	 * </pre>
	 * 
	 * @param fno      뷰로부터 가져온 파일 번호
	 * @param response 파일을 다운로드 받기 위한 HttpServletResponse 타입의 객체
	 * @throws Exception
	 */
	@RequestMapping(value = "/getFile")
	public void getFileController(@RequestParam("num") int fno, HttpServletResponse response) throws Exception {
		Map<String, Object> resultMap = service.getFile(fno);
		String storedFileName = (String) resultMap.get("new_filename");
		String originalFileName = (String) resultMap.get("org_filename");

		byte fileByte[] = org.apache.commons.io.FileUtils.readFileToByteArray(new File("C:\\Users\\wjddy\\Downloads\\file\\" + storedFileName));
		response.setContentType("application/octet-stream");
		response.setContentLength(fileByte.length);
		response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(originalFileName, "UTF-8") + "\";");
		response.getOutputStream().write(fileByte);
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}
}
