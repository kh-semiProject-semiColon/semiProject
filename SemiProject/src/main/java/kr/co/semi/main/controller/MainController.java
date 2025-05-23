package kr.co.semi.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.semi.main.service.MainService;
import kr.co.semi.member.model.dto.Member;
import kr.co.semi.studyboard.model.dto.Study;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@SessionAttributes("loginMember")
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
	public int stduyCreation(Study study, RedirectAttributes ra,@SessionAttribute("loginMember") Member loginMember) {
		
		// 로그인 안되어 있을 시 생성 불가
		if(loginMember == null) {
			String message = "로그인 후 이용해주세요";
			ra.addFlashAttribute(message);
			return -1;
		}
		// 스터디 생성 기능
		int result = service.studyCreation(study, loginMember.getMemberNo());
		
		return result;
	}
	
	@ResponseBody
	@PostMapping("/study/confirm")
	public int studyNameConfirm(@RequestBody Study study) {
		
		// 스터디명 중복 검사
		int studyNameConfirm = service.studyNameConfirm(study.getStudyName());
		log.info(study.getStudyName());
		
		return studyNameConfirm;
	}

}
