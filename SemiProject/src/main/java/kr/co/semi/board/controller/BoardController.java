package kr.co.semi.board.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.semi.board.model.service.BoardService;
import kr.co.semi.board.model.service.HireBoardService;
import kr.co.semi.studyboard.model.dto.Study;

@Controller
@RequestMapping("board")
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
	
	@GetMapping("announce")
	public String announceBoard(Model model,
    		@RequestParam(value="cp", required=false, defaultValue ="1") int cp, 
    		@RequestParam Map<String, Object> paramMap) {
		
Map<String, Object> map = null;
    	
    	if(paramMap.get("key") == null) {
    		
    		map = bService.selectAllAnnounce(cp);
    	}else {
    		map = bService.searchList(paramMap, cp);
    	}
    	
    	
    	model.addAttribute("announce",map.get("announce"));
    	model.addAttribute("pagination", map.get("pagination"));
		
		
		return "board/announce";
	}
}
