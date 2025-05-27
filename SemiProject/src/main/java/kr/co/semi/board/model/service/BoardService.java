package kr.co.semi.board.model.service;

import java.util.Map;

public interface BoardService {

	/** 공지사항 가져오기
	 * @param cp
	 * @return
	 */
	Map<String, Object> selectAllAnnounce(int cp);

	/** 검색어 포함된 공지사항 가져오기
	 * @param paramMap
	 * @param cp
	 * @return
	 */
	Map<String, Object> searchAnnounceList(Map<String, Object> paramMap, int cp);

	/** 게시판 글 목록 가져오기
	 * @param boardCode
	 * @param cp
	 * @return
	 */
	Map<String, Object> selectBoardList(int boardCode, int cp);

	/** 검색에 포함된 게시판 글 가져오기
	 * @param paramMap
	 * @param cp
	 * @return
	 */
	Map<String, Object> searchList(Map<String, Object> paramMap, int cp);

}
