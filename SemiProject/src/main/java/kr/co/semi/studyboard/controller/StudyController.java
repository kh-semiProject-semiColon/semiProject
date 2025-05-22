package kr.co.semi.studyboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpSession;

@Controller
public class StudyController {

    @GetMapping("/study/info/{studyNo}")
    public String infoPage(@PathVariable int studyNo, HttpSession session, Model model) {
        int memberNo = (int) session.getAttribute("loginMemberNo");
        // Study info fetch logic (simplified)
        model.addAttribute("selectedStudyNo", studyNo);
        return "studyboard/infoEdit";
    }
}
