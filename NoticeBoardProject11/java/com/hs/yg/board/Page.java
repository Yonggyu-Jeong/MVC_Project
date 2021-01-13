	package com.hs.yg.board;

public class Page {
	final private int POSTNUM = 10; // 한 페이지 번호에 표시할 페이지의 개수
	final private int PAGENUMCNT = 5; // 표시할 페이지 번호 개수 
	private int num; // JSP에서 가져온 페이지의 번호
	private int count; // 총 페이지의 개수
	private int startPageNum; // 표시하는 페이지 번호의 처음
	private int endPageNum; // 표시하는 페이지 번호의 끝
	
	private int displayPost; // sql에 사용, 현재 보고 있는 페이지에서 -10
	private String searchTypeKeyword; // &searchOption=searchOption&keyword=keyword 타입으로 문자열 반환

	private boolean prev;
	private boolean next;
	
	public void setNum(int num) {
		 this.num = num;
	}

	public void setCount(int count) {
		 this.count = count;
		 dataCalc();
	}

	public int getCount() {
		 return count;
	}

	public int getPostNum() {
		 return POSTNUM;
	}

	public int getDisplayPost() {
		 return displayPost;
	}

	public int getPageNumCnt() {
		 return POSTNUM;
	}

	public int getEndPageNum() {
		 return endPageNum;
	}

	public int getStartPageNum() {
		 return startPageNum;
	}

	public boolean getPrev() {
		 return prev;
	} 

	public boolean getNext() {
		 return next;
	}
		
	private void dataCalc() {
		 endPageNum = (int)(Math.ceil((double)num / (double)PAGENUMCNT) * PAGENUMCNT); // ceil(현재 페이지 수 / 표시할 페이지 번호 개수)  * 표시할 페이지 번호 개수 
		 startPageNum = endPageNum - (PAGENUMCNT - 1); // 표시하는 페이지 번호의 끝  - (표시할 페이지 번호 개수 - 1), endPageNum2를 만들면서 까지 하는 이유는 statPageNum을 정확하게 주기 위해 사용
		 
		 int endPageNum2 = (int)(Math.ceil((double)count / (double)POSTNUM)); // 반올림(총 페이지 개수) / 한 페이지 번호에 표시할 페이지의 개수
		 if(endPageNum > endPageNum2) { // 59번째 줄의 문제점은, 필요 이상의 page 갯수를 만들 수 있다는 점, 그러므로 61번째 줄의 
			 endPageNum = endPageNum2;
		 }
		 prev = startPageNum == 1 ? false : true;	// num이 1일 경우 prev는 false 처리되어, 나타나지 않음
		 next = endPageNum * POSTNUM >= count ? false : true; // endPageNum * 10이 총 게시글 수보다 크거나 같을 경우 next는 나오지 않음
		 displayPost = (num - 1) * POSTNUM;
	}
	
	public void setSearchTypeKeyword(String searchOption, String keyword) {
		if(searchOption.equals("") || keyword.equals("")) {
			searchTypeKeyword = ""; 
		} else {
			searchTypeKeyword = "&searchOption=" + searchOption + "&keyword=" + keyword; 
		}  
	}

	public String getSearchTypeKeyword() {
		return searchTypeKeyword;
	}
}
