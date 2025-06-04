package kr.co.semi.main.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.co.semi.board.model.dto.Announce;
import kr.co.semi.board.model.dto.Board;
import kr.co.semi.board.model.dto.HireInfo;
import kr.co.semi.common.util.Utility;
import kr.co.semi.main.service.MainService;
import kr.co.semi.member.model.dto.Member;
import kr.co.semi.studyboard.model.dto.Study;
import kr.co.semi.studyboard.model.service.StudyService;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@SessionAttributes("loginMember")
public class MainController {
	
	@Autowired
	private MainService service;
	
	@Autowired
	private StudyService stdService;
	
	
	@RequestMapping("/")
	public String mainPage(HttpServletRequest request, Model model) {
		
		HttpSession session = request.getSession(false);

	    if (session != null) {
	        String message = (String) session.getAttribute("message");
	        if (message != null) {
	            model.addAttribute("message", message); // request scope에 넣음
	            session.removeAttribute("message");     // 1회용 메시지니까 제거
	        }
	    }
	    
	    // 최신 공지사항 글 가져오기
	    Announce latestAnouncement = service.latestAnouncement();
	    
	    model.addAttribute("latestAnouncement",latestAnouncement);
	    
	    // 최신 구인게시글 가져오기
	    HireInfo latesttPost = service.latedstPost(); // 최신 구인글 조회
	    
	    model.addAttribute("latestPost",latesttPost); // 모델에 추가
	    
	    // 미니 스터디 현황을 위한 스터디 리스트 가져오기
	    List<Study> studyList = stdService.selectMainStudy(); // 스터디 리스트 조회
	    
	    model.addAttribute("studyList", studyList); // 모델에 추가
		
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
	public int stduyCreation(Study study, 
								RedirectAttributes ra
								,@SessionAttribute("loginMember") Member loginMember
								,@RequestParam(value = "profileImage", required = false) MultipartFile studyMainImg) {
		
		// 로그인 안되어 있을 시 생성 불가
		if(loginMember == null) {
			String message = "로그인 후 이용해주세요";
			ra.addFlashAttribute(message);
			return -1;
		}
		
		// 스터디 명 10글자 이상일 때 생성 불가
		if(study.getStudyName().length() > 10) {
			String message = "스터디 명은 10글자 이하로 작성해 주세요";
			ra.addFlashAttribute(message);
			return -2;
		}
		
		// 이미지가 존재하면 저장 처리
	    String uploadDir = "C:/uploadSemiFiles/studyProfile/";
	    String imagePath = null;
	    String savedFileName = null;
		
	    if (studyMainImg != null && !studyMainImg.isEmpty()) {
	        try {
	        	String originalFileName = studyMainImg.getOriginalFilename();
	        	savedFileName = Utility.fileRename(originalFileName);

	            File saveFile = new File(uploadDir + savedFileName);
	            saveFile.getParentFile().mkdirs();
	            studyMainImg.transferTo(saveFile);

	            imagePath = "/studyProfile/" + savedFileName;
	            study.setStudyMainImg(imagePath); // DTO에 경로 설정
	            
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	            return 0;
	        }
	    }
	    // 스터디 생성 기능
	    int result = service.studyCreation(study, loginMember.getMemberNo());
	    if(result>0) {
	    	loginMember.setStudyNo(result);
	    	result = 1;
	    }
	    
	    if (result == 0 && imagePath != null) {
	    	new File(uploadDir + savedFileName).delete();
	    }
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
