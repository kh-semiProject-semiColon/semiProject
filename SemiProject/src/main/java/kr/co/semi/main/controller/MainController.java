package kr.co.semi.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
	
	@RequestMapping("/")
	public String mainPage() {
		
		return "common/main";
	}
	
	@RequestMapping("/studyCreation")
	public String studyCreation() {
		return "study/study-creation";
	}

}
