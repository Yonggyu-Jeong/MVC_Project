package com.hs.yg.util;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.hs.yg.board.*;

@Component("fileUtils")
public class FileUtils {
	final private String filePath = "C:\\Users\\wjddy\\Downloads\\file\\"; // 파일이 저장될 위치
	final private String regExp = ".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*";

	/**
	 * <pre>
	 * 한글인 파일 원본 이름을 변환해주는 메소드
	 * </pre>
	 * 
	 * @param originalFileName      입력받은 파일 이름
	 * @param originalFileExtension 입력받은 파일 확장자
	 * @return
	 */
	public String reg(String originalFileName, String originalFileExtension) {
		if (originalFileName.matches(regExp) && ((originalFileExtension.equals(".jpg")) || (originalFileExtension.equals(".png")) || (originalFileExtension.equals(".gif")))) {
			originalFileName = "사진_" + getRandomString() + originalFileExtension;
		} else if (originalFileName.matches(regExp) && ((originalFileExtension.equals(".xls")) || (originalFileExtension.equals(".xlsx")) || (originalFileExtension.equals(".pdf"))
				|| (originalFileExtension.equals(".txt")) || (originalFileExtension.equals(".hwp")))) {
			originalFileName = "문서_" + getRandomString() + originalFileExtension;
		} else if (originalFileName.matches(regExp)) {
			originalFileName = "기타_" + getRandomString() + originalFileExtension;
		}
		return originalFileName;
	}

	/**
	 * <pre>
	 * 게시글을 적을 때 사용하는 파일 이름 및 관련 정보를 list 형식으로 반환하는 메소드
	 * </pre>
	 * 
	 * @param vo        게시판 번호
	 * @param mpRequest 파일 입출력을 사용하기 위한 MultipartHttpServletRequest 타입의 객체
	 * @return 파일에 관한 데이터를 List<Map<String, Object>>의 형태로 반환함
	 * @throws Exception
	 */
	public List<Map<String, Object>> parseInsertFileInfo(int bno, MultipartHttpServletRequest mpRequest) throws Exception {
		Iterator<String> iterator = mpRequest.getFileNames();
		MultipartFile multipartFile = null;
		String originalFileName = null;
		String originalFileExtension = null;
		String storedFileName = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> listMap = null;

		File file = new File(filePath);
		if (file.exists() == false) {
			file.mkdirs();
		}

		while (iterator.hasNext()) {
			multipartFile = mpRequest.getFile(iterator.next());
			if (multipartFile.isEmpty() == false) {
				originalFileName = new String(multipartFile.getOriginalFilename());
				originalFileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
				originalFileName = reg(originalFileName, originalFileExtension);

				storedFileName = getRandomString() + originalFileExtension;
				file = new File(filePath + storedFileName);
				multipartFile.transferTo(file);
				listMap = new HashMap<String, Object>();
				listMap.put("BNO", bno);
				listMap.put("ORG_FILE_NAME", originalFileName);
				listMap.put("STORED_FILE_NAME", storedFileName);
				listMap.put("FILE_SIZE", multipartFile.getSize());
				list.add(listMap);
			}
		}
		return list;
	}

	/**
	 * <pre>
	 * parseInsertFileInfo의 오버로딩
	 * </pre>
	 * 
	 * @param vo        뷰로부터 가져온 Board 타입의 데이터
	 * @param mpRequest 파일 입출력을 사용하기 위한 MultipartHttpServletRequest 타입의 객체
	 * @return 파일에 관한 데이터를 List<Map<String, Object>>의 형태로 반환함
	 * @throws Exception
	 */
	public List<Map<String, Object>> parseInsertFileInfo(Board vo, MultipartHttpServletRequest mpRequest) throws Exception {
		Iterator<String> iterator = mpRequest.getFileNames();
		MultipartFile multipartFile = null;
		String originalFileName = null;
		String originalFileExtension = null;
		String storedFileName = null;
		int bno = vo.getBno();

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> listMap = null;

		File file = new File(filePath);
		if (file.exists() == false) {
			file.mkdirs();
		}

		while (iterator.hasNext()) {
			multipartFile = mpRequest.getFile(iterator.next());

			if (multipartFile.isEmpty() == false) {
				originalFileName = new String(multipartFile.getOriginalFilename());
				originalFileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
				originalFileName = reg(originalFileName, originalFileExtension);
				storedFileName = getRandomString() + originalFileExtension;

				file = new File(filePath + storedFileName);
				multipartFile.transferTo(file);
				listMap = new HashMap<String, Object>();
				listMap.put("BNO", bno);
				listMap.put("ORG_FILE_NAME", originalFileName);
				listMap.put("STORED_FILE_NAME", storedFileName);
				listMap.put("FILE_SIZE", multipartFile.getSize());
				list.add(listMap);
			}
		}
		return list;
	}

	/**
	 * <pre>
	 * 게시글을 업데이트할 때 사용하는 파일 이름 및 관련 정보를 list 형식으로 반환하는  메소드
	 * </pre>
	 * 
	 * @param vo        뷰로부터 가져온 Board 타입의 데이터
	 * @param files     뷰로부터 가져온 String 배열 타입의 파읾 번호
	 * @param fileNames 뷰로부터 가져온 String 배열 타입의 파읾 번호
	 * @param mpRequest 파일 입출력을 사용하기 위한 MultipartHttpServletRequest 타입의 객체
	 * @return 파일에 관한 데이터를 List<Map<String, Object>>의 형태로 반환함
	 * @throws Exception
	 */
	public List<Map<String, Object>> parseUpdateFileInfo(Board vo, String[] files, String[] fileNames, MultipartHttpServletRequest mpRequest) throws Exception {
		Iterator<String> iterator = mpRequest.getFileNames();
		MultipartFile multipartFile = null;
		String originalFileName = null;
		String originalFileExtension = null;
		String storedFileName = null;

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> listMap = null;
		while (iterator.hasNext()) {
			multipartFile = mpRequest.getFile(iterator.next());
			if (multipartFile.isEmpty() == false) {
				originalFileName = new String(multipartFile.getOriginalFilename());
				originalFileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
				originalFileName = reg(originalFileName, originalFileExtension);
				storedFileName = getRandomString() + originalFileExtension;
				multipartFile.transferTo(new File(filePath + storedFileName));
				listMap = new HashMap<String, Object>();
				listMap.put("BNO", vo.getBno());
				listMap.put("IS_NEW", "Y");
				listMap.put("ORG_FILE_NAME", originalFileName);
				listMap.put("STORED_FILE_NAME", storedFileName);
				listMap.put("FILE_SIZE", multipartFile.getSize());
				list.add(listMap);
			}
		}
		if (files != null && fileNames != null) {
			for (int i = 0; i < fileNames.length; i++) {
				listMap = new HashMap<String, Object>();
				listMap.put("FILE_NO", files[i]);
				listMap.put("IS_NEW", "F");
				list.add(listMap);
			}
		}
		return list;
	}

	/**
	 * <pre>
	 * 파일 이름을 중복되지 않는 이름으로 바꿔주는 스태틱 메소드
	 * </pre>
	 * 
	 * @return 랜덤으로 생성된 String 타입의 UUID를 반환함
	 */
	public static String getRandomString() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}