package kr.co.semi.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.semi.board.model.dto.Comment;
import kr.co.semi.board.model.service.CommentService;
import lombok.extern.slf4j.Slf4j;

@RestController 
@RequestMapping("comments")
@Slf4j
public class CommentController {

	
	@Autowired
	private CommentService service;
	
	
	/** 댓글 목록 조회
	 * @param boardNo
	 * @return
	 */
	@GetMapping("")
	public List<Comment> select(@RequestParam("boardNo") int boardNo) {
		// List<Comment> (Java의 자료형 List)
		// HTTPMessageConverter가
		// List -> JSON(문자열)로 변환해서 응답 -> JS
		
		
		
		return service.select(boardNo);
		
	}
	
	/** 댓글/답글 등록
	 * @return
	 */
	@PostMapping("")
	public int insert(@RequestBody Comment comment) {
		
		return service.insert(comment);
	}
	
	@DeleteMapping("")
	public int delete(@RequestBody int commentNo) {
		return service.delete(commentNo);
	}
	
	@PutMapping("")
	public int update(@RequestBody Comment comment) {
		return service.update(comment);
	}
}
