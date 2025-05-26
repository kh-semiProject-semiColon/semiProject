package kr.co.semi.board.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.ModelAttribute;

import kr.co.semi.board.model.dto.HireInfo;
import kr.co.semi.board.model.service.HireBoardService;
import kr.co.semi.member.model.dto.Member;
import kr.co.semi.studyboard.model.dto.Study;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("board")
@Slf4j
public class BoardController {
	
	@Autowired
	private HireBoardService service;
	@Autowired
	private BoardService bService;

	/** 구인 게시판 조회 
	 * @return
	 */
	@GetMapping("hire")
	public String showHireBoard(@RequestParam(value = "cp", required = false, defaultValue = "1") int cp,
							    Model model,
							    @RequestParam Map<String, Object> paramMap) {
		
		Map<String, Object> map = null;
		
		if(paramMap.get("key") == null) {
			map = service.showHireBoard(cp);
		} else {

			map = service.searchList(paramMap, cp);
		}
		
		model.addAttribute("pagination", map.get("pagination"));
		model.addAttribute("hireList", map.get("hireList"));
		
	
		return "hire/hireBoard";
	}
	
	@GetMapping("detail")
	public String showBoardDetail() {
		
		return "hire/boardDetail";
	}
	
	@GetMapping("write")
	public String writeHireBoard(@SessionAttribute("loginMember") Member loginMember,
			  					  Model model,
			  					 RedirectAttributes ra) {
		String path;
		
		int memberNo = loginMember.getMemberNo();
		
	    List<Study> studyList = service.showStudySelect(memberNo);
	    
	    
	    if(studyList.isEmpty() || studyList == null) {
	    	path = "redirect:/board/hire"; // 목록 재요청
	    	ra.addFlashAttribute("message", "생성된 스터디가 없습니다.");
	    	
	    } else {
	    	
	    	model.addAttribute("study", studyList);
	    	path = "hire/hireWrite";
	    	
	    }
	    
	    
		return path;
	}
	
//	@PostMapping("write")
//	public String writehireBoardInsert(@ModelAttribute HireInfo inputHire,
//									   @SessionAttribute("loginMember") Member loginMember,
//									   RedirectAttributes ra
//									   ) {
//		
//		
//		
//		return "redirect:/board/hire";
//	}
//	
}
