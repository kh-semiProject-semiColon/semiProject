package kr.co.semi.studyboard.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.semi.studyboard.model.service.StudyBoardService;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/studyboard")
@Slf4j
public class StudyBoardController {

	@Autowired
	private StudyBoardService service;
	
	
	public String studyBoardMain() {
		return "studyboard/studyboardList"; //메인 페이지
	}
	/** 
	 * @param studyNo : 게시판 종류 구분 번호 (1/2/3....)
	 * @param cp : 현재 조회 요청한 페이지 번호 (없으면 1)
	 * @param model
	 * @param paraMap
	 * @return
	 */
	@GetMapping("/studyboardList")
	public String selectStudyBoardList(@PathVariable("studyNo")int studyNo,
										@RequestParam(value = "cp",required = false,defaultValue = "1")int cp,
										Model model ,
										@RequestParam Map<String, Object> paraMap) {	
		
		//조회 서비스 호출 후 결과 반환 받기.
		Map<String, Object> map = null;
		
		// 조건에 따라 서비스 메서드를 분기처리 하기 위해 map으로 선언만 함.
		
		//검색이 아닌 경우
		if(paraMap.get("key")==null) {
			// 게시글 목록 조회 서비스 호출
			map = service.selectStudyBoardList(studyNo,cp);
		}else {
			// 검색인 경우 --> paramMap = {
			
			// boardCode 를 paramMap에 추가
			paraMap.put("studyNo", studyNo );
			
			// 검색 서비스 호출
			map = service.searchList(paraMap,cp);
		}
		
		// model에 반환 받은 값 등록
		model.addAttribute("pagination",map.get("pagination"));
		model.addAttribute("studyboardList",map.get("studboardList"));
				
		// forward : src/main/resources/templates/studyboard/studyboardList.html
		return "studyboard/studyboardList";
	}
	//상세 조회 요청 주소
	//  /board/1/1994?cp=1
	//  /board/2/2000?cp=2	
}
