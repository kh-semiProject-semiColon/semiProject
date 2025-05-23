package kr.co.semi.studyboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import jakarta.servlet.http.HttpSession;
import kr.co.semi.member.controller.MemberController;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("study")
@Slf4j
@SessionAttributes({"loginMember"})
@Controller
public class StudyController {

    @GetMapping("/study/info/{studyNo}")
    public String infoPage(@PathVariable int studyNo, HttpSession session, Model model) {
        int memberNo = (int) session.getAttribute("loginMemberNo");
        // Study info fetch logic (simplified)
        model.addAttribute("selectedStudyNo", studyNo);
        return "studyboard/infoEdit";
    }
    
    
    @GetMapping("studyNow")
    public String studyNow() {
    	return"study/studyNow";
    }
    
}
