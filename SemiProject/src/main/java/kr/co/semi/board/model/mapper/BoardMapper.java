package kr.co.semi.board.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import kr.co.semi.board.model.dto.Announce;
import kr.co.semi.board.model.dto.Board;
import kr.co.semi.board.model.dto.HireInfo;
import kr.co.semi.studyboard.model.dto.Study;

@Mapper
public interface BoardMapper {

	/** 게시글 수 조회
	 * @param hireNo
	 */
	int getListCount();

	/** 지정된 페이지 목록 조회
	 * @param rowBounds
	 * @return
	 */
	List<HireInfo> selecHireBoardList(RowBounds rowBounds);

	/** 검색 서비스
	 * @param paramMap
	 * @return
	 */
	int getSerchCount(Map<String, Object> paramMap);

	/** 내 스터디 조회
	 * @return
	 */
	List<Study> showStudySelect(int memberNo);
	/** 삭제되지 않은 공지사항 게시글 개수
	 * @return
	 */
	int getAnnounceCount();

	/** 모든 공지 가져오기
	 * @param rowBounds
	 * @return
	 */
	List<Announce> selectAllAnnounce(RowBounds rowBounds);

	/** 검색 조건을 포함한 삭제되지 않은 공지글 개수
	 * @param paramMap
	 * @return
	 */
	int getAnnounceSearchCount(Map<String, Object> paramMap);

	/** 검색 조건을 포함한 공지사항 정보
	 * @param paramMap
	 * @param rowBounds
	 * @return
	 */
	List<Announce> selectAnnounceSearchList(Map<String, Object> paramMap, RowBounds rowBounds);

	/** 일반 게시글 목록 가져오기
	 * @param boardCode
	 * @param rowBounds
	 * @return
	 */
	List<Board> selectBoardList(int boardCode, RowBounds rowBounds);

	/** 삭제되지 않은 게시판글 수
	 * @param boardCode
	 * @return
	 */
	int getBoardListCount(int boardCode);

	/** 삭제되지 않은 검색어를 포함한 게시글 수
	 * @param paramMap
	 * @return
	 */
	int getBoardSearchCount(Map<String, Object> paramMap);

	/** 삭제되지 않은 검색어를 포함한 게시글 정보
	 * @param paramMap
	 * @param rowBounds
	 * @return
	 */
	List<Board> selectBoardSearchList(Map<String, Object> paramMap, RowBounds rowBounds);

	/** 구인 게시글 작성
	 * @param inputHire
	 * @return
	 */
	int writeHireBoardInsert(HireInfo inputHire);

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

	/** 조회수 조회
	 * @param boardNo
	 * @return
	 */
	int selectReadCount(int boardNo);


	

}
