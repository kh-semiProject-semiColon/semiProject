package kr.co.semi.board.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
	public Map<String, Object> select(@RequestBody HireComment hireComment) {
		// List<Comment> (Java의 자료형 List)
		// HTTPMessageConverter가
		// List -> JSON(문자열)로 변환해서 응답 -> JS
//		log.info("댓글목록조회 : "+service.select(hireComment.getHireNo()));
		
		System.out.println(hireComment);
		
		List<HireComment> hireList =  service.select(hireComment);
		
		int currentCount = service.selectCurrentCount(hireComment.getStudyNo());
		
		int maxCount = service.selectMaxCount(hireComment.getStudyNo());
		
		Map<String, Object> map = new HashMap();
		
		map.put("hireList", hireList);
		map.put("studyMaxCount", maxCount);
		map.put("currentCount", currentCount);
		
		System.out.println(hireList);
		
		return map;
		
	}
	
	/** 댓글/답글 등록
	 * @return
	 */
	@PostMapping("")
	public int insert(@RequestBody HireComment hireComment,@SessionAttribute("loginMember") Member loginMember) {
		log.info("댓글조회 : " + hireComment);
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
