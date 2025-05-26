package kr.co.semi.board.model.service;

import java.util.List;
import java.util.Map;

import kr.co.semi.member.model.dto.Member;
import kr.co.semi.studyboard.model.dto.Study;

public interface HireBoardService {
	
	// 구인 게시글 조회
	Map<String, Object> showHireBoard(int cp);

	// 구인 게시글 검색
	Map<String, Object> searchList(Map<String, Object> paramMap, int cp);

	// 내 스터디 조회
	List<Study> showStudySelect(int memberNo);

	

}
