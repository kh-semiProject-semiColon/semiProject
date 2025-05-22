package kr.co.semi.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.semi.main.service.MainService;
import kr.co.semi.studyboard.model.dto.Study;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MainController {
	
	@Autowired
	private MainService service;
	
	
	@RequestMapping("/")
	public String mainPage() {
		
		return "common/main";
	}
	
	@RequestMapping("/studyCreation")
	public String studyCreation() {
		return "study/study-creation";
	}
	
	/** 스터디 생성 by 김성원
	 * @param study
	 * @param ra
	 * @return
	 */
	@PostMapping("/study/create")
	@ResponseBody
	public int stduyCreation(Study study, RedirectAttributes ra) {
		log.info(study.toString());
		int result = service.studyCreation(study);
		return result;
	}

}
