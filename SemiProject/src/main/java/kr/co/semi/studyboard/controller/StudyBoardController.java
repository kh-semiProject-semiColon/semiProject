package kr.co.semi.studyboard.controller;

import java.security.Provider.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.semi.studyboard.model.dto.Study;
import kr.co.semi.studyboard.model.service.StudyBoardService;
import kr.co.semi.studyboard.model.service.StudyService;

/**
 * 🎯 StudyBoardController
 * 📌 스터디 게시판 관련 요청을 처리하는 컨트롤러 클래스
 * 🔗 연동 구조: studyboardList.html, studyboardDetail.html 등과 연결됨
 */
@Controller
@RequestMapping("studyBoard")
public class StudyBoardController {

	@Autowired
    private StudyBoardService service;
    
    @GetMapping("calendar")
    public String studyBoardCalendar() {
    	return "studyBoard/calendar";
    }

    @GetMapping("studyboard")
    public String studyBoard(){
    	return "studyBoard/studyboard";
    }
    
    @GetMapping("update")
    public String studyBoardUpdate() {
    	return "studyBoard/update";
    }

    @GetMapping("delete")
    public String studyBoardDelete() {
    	return "studyBoard/delete";
    }
    @GetMapping("delete1")
    public String studyBoardDelete1() {
    	return "studyBoard/delete1";
    }
    
    @GetMapping("rulecontent")
    public String studyBoardrulecontent(@RequestParam("studyNo") int studyNo, Model model) {
    	
    	Study study = service.selectStudyById(studyNo);
        model.addAttribute("study", study); // ✅ 필수
    	return "studyBoard/rulecontent";
    }

}
