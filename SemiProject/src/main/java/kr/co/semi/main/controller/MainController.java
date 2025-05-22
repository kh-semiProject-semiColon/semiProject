package kr.co.semi.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.semi.main.service.MainService;
import kr.co.semi.studyboard.model.dto.Study;

@Controller
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
	
	@RequestMapping("/study/creation")
	@ResponseBody
	public int stduyCreation(Study study, RedirectAttributes ra) {
		int result = service.studyCreation(study);
		return result;
	}

}
