package kr.co.semi.board.model.service;

import java.util.Map;

public interface HireBoardService {
	
	// 구인 게시글 조회
	Map<String, Object> showHireBoard(int cp);

	// 구인 게시글 검색
	Map<String, Object> searchList(Map<String, Object> paramMap, int cp);

}
