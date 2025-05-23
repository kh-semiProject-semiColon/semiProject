package kr.co.semi.studyboard.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import jakarta.servlet.http.HttpSession;
import kr.co.semi.studyboard.model.dto.Study;
import kr.co.semi.studyboard.model.service.StudyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("study")
@Slf4j
@SessionAttributes({"loginMember"})
@Controller
@RequiredArgsConstructor
public class StudyController {
	
	private final StudyService service;

    @GetMapping("/study/info/{studyNo}")
    public String infoPage(@PathVariable int studyNo, HttpSession session, Model model) {
        int memberNo = (int) session.getAttribute("loginMemberNo");
        // Study info fetch logic (simplified)
        model.addAttribute("selectedStudyNo", studyNo);
        return "studyboard/infoEdit";
    }
    
    
    @GetMapping("studyNow")
    public String studyNow(Model model) {
    	
    	List<Study> study = service.selectAllStudy();
    	
    	List<Study> capMember = service.selectCap();
    	
    	List<Study> countMember = service.countMember();
    	
    	Map<Integer, Integer> memberCount = new HashMap();
    	
    	for(Study s : countMember) {
    		
    		int studyNo = s.getStudyNo();
    		int studyMemberCount = s.getMemberCount();
    		
    		memberCount.put(studyNo, studyMemberCount);
    	}
    	
    	model.addAttribute("study",study);
    	model.addAttribute("cap",capMember);
    	model.addAttribute("memberCount", memberCount);
    	
    	return"study/studyNow";
    }
    
}
