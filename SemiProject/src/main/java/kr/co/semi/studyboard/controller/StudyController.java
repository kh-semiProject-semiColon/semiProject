package kr.co.semi.studyboard.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String studyNow(Model model,
    		@RequestParam(value="cp", required=false, defaultValue ="1") int cp, 
    		@RequestParam Map<String, Object> paramMap) {
    	
    	Map<String, Object> map = null;
    	
    	if(paramMap.get("key") == null) {
    		
    		map = service.selectAllStudy(cp);
    	}else {
    		map = service.searchList(paramMap, cp);
    	}
    	
    	List<Study> capMember = service.selectCap();
    	
    	
    	
    	model.addAttribute("study",map.get("study"));
    	model.addAttribute("cap",capMember);
    	model.addAttribute("pagination", map.get("pagination"));
    	
    	return"study/studyNow";
    }
    
}
