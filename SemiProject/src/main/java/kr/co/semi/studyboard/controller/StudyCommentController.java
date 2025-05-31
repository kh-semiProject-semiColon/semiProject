package kr.co.semi.studyboard.controller;

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
import kr.co.semi.studyboard.model.dto.StudyComment;
import kr.co.semi.studyboard.model.service.StudyCommentService;
import lombok.extern.slf4j.Slf4j;

@RestController 
@RequestMapping("studyComments")
@Slf4j
public class StudyCommentController {

	
	@Autowired
	private StudyCommentService service;
	
	
	/** 댓글 목록 조회
	 * @param boardNo
	 * @return
	 */
	@GetMapping("")
	public List<StudyComment> select(@RequestParam("studyBoardNo") int studyBoardNo) {
		// List<Comment> (Java의 자료형 List)
		// HTTPMessageConverter가
		// List -> JSON(문자열)로 변환해서 응답 -> JS
		
		
		
		return service.select(studyBoardNo);
		
	}
	
	/** 댓글/답글 등록
	 * @return
	 */
	@PostMapping("")
	public int insert(@RequestBody StudyComment studyComment) {
		
		return service.insert(studyComment);
	}
	
	@DeleteMapping("")
	public int delete(@RequestBody int studyCommentNo) {
		return service.delete(studyCommentNo);
	}
	
	@PutMapping("")
	public int update(@RequestBody StudyComment studyComment) {
		return service.update(studyComment);
	}
}
