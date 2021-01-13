	package com.hs.yg.util;

import java.io.File;
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
	private static final String filePath = "C:\\mp\\file\\"; // 파일이 저장될 위치
	
	public List<Map<String, Object>> parseInsertFileInfo(Board vo, 
			MultipartHttpServletRequest mpRequest) throws Exception{
		
		/*
			Iterator은 데이터들의 집합체? 에서 컬렉션으로부터 정보를 얻어올 수 있는 인터페이스입니다.
			List나 배열은 순차적으로 데이터의 접근이 가능하지만, Map등의 클래스들은 순차적으로 접근할 수가 없습니다.
			Iterator을 이용하여 Map에 있는 데이터들을 while문을 이용하여 순차적으로 접근합니다.
		*/
		
		Iterator<String> iterator = mpRequest.getFileNames();
		
		MultipartFile multipartFile = null;
		String originalFileName = null;
		String originalFileExtension = null;
		String storedFileName = null;
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String, Object> listMap = null;
		
		File file = new File(filePath);
		if(file.exists() == false) {
			file.mkdirs();	//경로에 파일이 없다면 폴더 생성
		}
		
		while(iterator.hasNext()) {
			multipartFile = mpRequest.getFile(iterator.next()); // iterator에 파일이 있을 경우, mpRequest에서 가져온 파일 이름으로 mpRequest.getFile을 가져온다.
			if(multipartFile.isEmpty() == false) { 
				originalFileName = multipartFile.getOriginalFilename(); // multipartfile을 가져왔다면, 오리지널 이름을 저장한다.
				originalFileExtension = originalFileName.substring(originalFileName.lastIndexOf(".")); //파일 확장자 분리
				storedFileName = getRandomString() + originalFileExtension; //랜덤 uuid와 확장자를 결합
				
				file = new File(filePath + storedFileName); // 경로에 해당되는 곳에 새로운 파일을 생성한다.
				multipartFile.transferTo(file); //파일을 파일 경로에 저장
				listMap = new HashMap<String, Object>();
				listMap.put("ORG_FILE_NAME", originalFileName);	//파일의 원래 이름을 저장
				listMap.put("STORED_FILE_NAME", storedFileName); //파일의 새로운 이름을 저장
				listMap.put("FILE_SIZE", multipartFile.getSize()); //파일의 크기를 저장(long)
				list.add(listMap);
			}
		}
		return list;
	}
	
	public static String getRandomString() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}