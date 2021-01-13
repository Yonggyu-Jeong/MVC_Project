package com.hs.yg.board.controller;

import java.sql.SQLException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.hs.yg.board.Board;
import com.hs.yg.board.Page;
import com.hs.yg.board.Reply;
import com.hs.yg.board.service.BoardService;

@Controller
public class BoardController {

	@Autowired
	BoardService service;
	
	// * 처음 페이지, index.jsp로 이동
	// index -> index.jsp -> listPage.jsp
	@RequestMapping("/")
	public String goIndex() {
		return "index"; 
	}	
			
	// * 게시글 페이징 구현, listPage.jsp에서 num을 입력받으며 Page page와 List<board> pageList를 받아와 listPage.jsp에 전달
	// page에 전달받은 페이지 번호와 service.getBoardCnt()를 통해 얻은 게시글 수를 통해 page의 변수들을 초기화함 -> 
	// service.getListPage()에 초기화한 변수인 displatPost와 postNum을 전달 후 결과를 lPage에 전달 ->
	// model.addAttribute를 이용해 lPage를 boardList, page를 page, num을 select로 listPage.jsp에 전달함.
	// listPage -> listPage.jsp -> content or searchListPage or listPage(페이지 이동)
	@RequestMapping("/listPage") 
	public void listpage(Model model, @RequestParam(value = "num", defaultValue = "1") int num) throws Exception {
		 Page page = new Page();	 
		 page.setNum(num);
		 page.setCount(service.getBoardCnt());  
		 List<Board> lPage = service.getListPage(page.getDisplayPost(), page.getPostNum());
		 model.addAttribute("boardList", lPage);   
		 model.addAttribute("page", page);
		 model.addAttribute("select", num);		 
	}
	
	// * 검색한 게시글 페이징 구현, searchListPage.jsp에서  searchOption과 keyword 그리고 num을 입력받으며, Page page와 List<board> list로 받아와 searchListPage.jsp에 전달
	// page에 전달받은 페이지 번호와 service.getSearchedBoardCnt()를 통해 얻은 검색한 게시글 수를 통해 page의 변수들을 초기화함 -> 
	// service.getListPage()에  searchOption, keyword, displatPost와 postNum을 전달 후 결과를 sLPage에 전달 ->
	// model.addAttribute를 이용해 sLPage를 boardList, page를 page, num을 select로 searchOption과 keyword를 본래 이름으로 searchListPage.jsp에 전달함.
	// searchListPage -> searchListPage.jsp -> content or searchListPage(페이지 이동) or listPage
	@RequestMapping(value = "/searchListPage")
	public void getBoardSearchList(@RequestParam(value = "searchOption",required = false, defaultValue = "title") String searchOption,
		@RequestParam(value = "keyword",required = false, defaultValue = "") String keyword, 
		@RequestParam(value = "num", defaultValue = "1") int num, Model model) throws SQLException {
		Page page = new Page();	 
		page.setNum(num);
		page.setCount(service.getSearchedBoardCnt(searchOption, keyword));  
		List<Board> sLPage = service.getSearchedBoardList(searchOption, keyword, page.getDisplayPost(), page.getPostNum());
		page.setSearchTypeKeyword(searchOption, keyword);
		model.addAttribute("boardList", sLPage);
		model.addAttribute("page", page);
		model.addAttribute("select", num);
		model.addAttribute("searchOption", searchOption);
		model.addAttribute("keyword", keyword);	
	}

	// * 글 상세 보기, listaPage.jsp 또는 searchListPage.jsp에서 bno를 입력 받아와, Board board와 List<Reply> replyList에 글과 댓글 데이터를 저장한 뒤 content.jsp에 전달
	// content -> content.jsp -> writeReply or secret or move or delete or listPage
	@RequestMapping("/content") 
	public String getBoard(Board vo, Model model) throws SQLException {
        Board board = service.getContent(vo);
        List<Reply> replyList = service.getReplyList(vo.getBno());
		model.addAttribute("board", board); 
		model.addAttribute("reply", replyList);
		return "content"; 
	}
	
	// * 비밀글 확인, content.jsp에서 num과 tpasswd를 입력 받아, bno와 passwd로 저장한다. Board board와 List<Reply> replyList에 글과 댓글 데이터를 저장.
	//board.passwd와  passwd가 일치하다면 board와 replyList의 정보를 model.addAttribute를 이용해 board와 replyList를 sboard와 sreply라는 이름으로 content.jsp에 전달
	//secret -> secret.jsp -> writeReply or move or delete or listPage
	@RequestMapping("/secret")
	public String getBoard(@RequestParam("num") int bno, @RequestParam("tpasswd") String passwd, Model model) throws SQLException {
        Board board = service.getContent(bno);
        List<Reply> replyList = service.getReplyList(bno);
        if(board.getPasswd().equals(passwd)) {
        	model.addAttribute("sboard", board); 
        	model.addAttribute("sreply", replyList);
        }
		return "secretPage"; 
	}
	
	// * 글 쓰기, inseartBoard.jsp에서 name, passwd, title, content, file을 입력 받아, MultipartHttpServletRequest를 통해 글과 파일을 각 각의 데이터베이스에 저장.
	// dao inseartBoard를 통해 dBoard와 dBoard_files에 글과 파일을 저장한 후, listPage로 리다이렉트한다.
	@RequestMapping(value = "/insert", method = RequestMethod.POST) 
	public String insertBoard(Board vo, MultipartHttpServletRequest mpRequest) throws Exception { 
		service.insertBoard(vo, mpRequest); 
		return "redirect:listPage"; 
	}
	
	// * 글 쓰기 페이지 이동, content.jsp or secretPage.jsp에서 하이퍼링크를 통해 이동, inseatBoard.jsp로 이동함
	// move -> insertBoard.jsp
	@RequestMapping("/move") 
	public String moveInsertBoard()throws Exception{
		return "insertBoard";
	}
	
	// * 글 삭제,  content.jsp or secretPage.jsp에서 하이퍼링크를 통해 이동, delete 컨트롤로 이동시 int bno를 가져오며, deleteBoard를 통해 해당하는 bno의 파일, 댓글, 글 순으로 삭제함
	// dao deleteBoard를 통해 dBoard, dBoard_files, dBoard_reply의 데이터를 삭제한 후,  listPage로 리다이렉트한다.
	@RequestMapping("/delete") 
	public String deleteBoard(Board vo) throws SQLException {
		service.deleteBoard(vo);
		return "redirect:listPage";
	}
	
	// * 댓글 쓰기 ,  content.jsp or secretPage.jsp에서 rwriter와 rcontent 그리고 bno를 입력받는다. 
	//HttpServletRequest request를 통해 Reply vo의 변수에 저장한 뒤, insertReply를 실행해 dBoard_reply에 해당 정보를 입력한 후, listPage로 리다이렉트한다.
	@RequestMapping("/writeReply") 
	public String writeReply(Reply vo, HttpServletRequest request) throws SQLException {
		vo.setRbno(Integer.parseInt(request.getParameter("bno")));
		vo.setRcontent(request.getParameter("rcontent"));
		vo.setRwriter(request.getParameter("rwriter"));
		service.insertReply(vo);
		return "redirect:listPage";
	}
	
	// 더 이상 사용하지 않는 컨트롤러 ------------------------------------------------------------------------------------------------------------------
	
	 // 검색한 게시글 목록
	@RequestMapping("/search") 
	public String getSearchedBoardList(@RequestParam("condition") String searchOption, 
			@RequestParam("keyword") String keyword, @RequestParam(value = "num", defaultValue = "1")
			int num, Model model) throws SQLException {
		Page page = new Page();	 
		page.setNum(num);
		page.setCount(service.getSearchedBoardCnt(searchOption, keyword)); 
		List<Board> boardList = service.getSearchedBoardList(searchOption, keyword, page.getDisplayPost(), page.getPostNum());
		model.addAttribute("boardList", boardList);
		model.addAttribute("page", page);
		model.addAttribute("select", num);
		model.addAttribute("searchOption", searchOption);
		model.addAttribute("keyword", keyword);		
		return "searchListPage"; 
	}
	
	// 게시글 목록
	@RequestMapping("/list") 
	public String getBoardList(Board vo, Model model) throws SQLException {
		List<Board> boardList = service.getBoardList(vo);
		model.addAttribute("boardList", boardList);
		return "listPage"; 
	}
}
