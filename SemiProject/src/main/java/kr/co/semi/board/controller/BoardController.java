package kr.co.semi.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("board")
public class BoardController {

	@GetMapping("hire")
	public String showHireBoard() {
	
		return "hire/hireBoard";
	}
	
	@GetMapping("detail")
	public String showBoardDetail() {
		
		return "hire/boardDetail";
	}
}
