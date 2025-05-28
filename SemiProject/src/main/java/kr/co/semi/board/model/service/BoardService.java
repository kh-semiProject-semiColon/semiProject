package kr.co.semi.board.model.service;

import java.util.Map;

import kr.co.semi.board.model.dto.Board;

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

	/** 게시글 상세조회
	 * @param map
	 * @return
	 */
	Board selectOne(Map<String, Integer> map);

	/** 조회수 증가
	 * @param boardNo
	 * @return
	 */
	int updateReadCount(int boardNo);

	/** 좋아요 체크
	 * @param map
	 * @return
	 */
	int boardLike(Map<String, Integer> map);

}
