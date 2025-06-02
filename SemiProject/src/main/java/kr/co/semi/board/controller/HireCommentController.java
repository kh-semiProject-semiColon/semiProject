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
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import kr.co.semi.board.model.dto.HireComment;
import kr.co.semi.board.model.service.HireCommentService;
import kr.co.semi.member.model.dto.Member;
import lombok.extern.slf4j.Slf4j;

@RestController 
@RequestMapping("comment")
@SessionAttributes("loginMember")
@Slf4j
public class HireCommentController {

	
	@Autowired
	private HireCommentService service;
	
	// 댓글 목록 조회
	@PostMapping("select")
	public List<HireComment> select(@RequestBody HireComment hireComment) {
		// List<Comment> (Java의 자료형 List)
		// HTTPMessageConverter가
		// List -> JSON(문자열)로 변환해서 응답 -> JS
//		log.info("댓글목록조회 : "+service.select(hireComment.getHireNo()));
		return service.select(hireComment.getHireNo());
		
	}
	
	/** 댓글/답글 등록
	 * @return
	 */
	@PostMapping("")
	public int insert(@RequestBody HireComment hireComment,@SessionAttribute("loginMember") Member loginMember) {
		hireComment.setMemberNo(loginMember.getMemberNo());
//		log.info("댓글조회 - hireNo: " + hireComment.getHireCommentContent());
		return service.insert(hireComment);
	}
	
	@DeleteMapping("")
	public int delete(@RequestBody int hireCommentNo) {
		return service.delete(hireCommentNo);
	}
	
	@PutMapping("")
	public int update(@RequestBody HireComment hireComment) {
		return service.update(hireComment);
	}
}
