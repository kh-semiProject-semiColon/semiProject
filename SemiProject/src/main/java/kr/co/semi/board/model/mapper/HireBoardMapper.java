package kr.co.semi.board.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import kr.co.semi.board.model.dto.HireInfo;
import kr.co.semi.studyboard.model.dto.Study;

@Mapper
public interface HireBoardMapper {

	/** 게시글 수 조회
	 * @param hireNo
	 */
	int getListCount();
	
	/** 검색 서비스
	 * @param paramMap
	 * @return
	 */
	int getSerchCount(Map<String, Object> paramMap);

	/** 지정된 페이지 목록 조회
	 * @param rowBounds
	 * @return
	 */
	List<HireInfo> selecHireBoardList(RowBounds rowBounds);

	/** 내 스터디 조회
	 * @return
	 */
	List<Study> showStudySelect(int memberNo);
	
	/** 구인 게시글 작성
	 * @param inputHire
	 * @return
	 */
	int writeHireBoardInsert(HireInfo inputHire);
	
	/** 구인 게시글 상세 조회
	 * @param map
	 * @return
	 */
	HireInfo selectOne(Map<String, Integer> map);

	
	/** 구인 게시글 조회수 증가
	 * @param hireNo
	 * @return
	 */
	int updateHireReadCount(int hireNo);

	/** 구인 게시글 스터디 조회
	 * @param studyNo
	 * @return
	 */
	Study selectStudyNo(int studyNo);

	/** 구인 게시글 수정
	 * @param inputHire
	 * @return
	 */
	int hireUpdate(HireInfo inputHire);

	/** 구인 게시글 삭제
	 * @param map
	 * @return
	 */
	int hireDelete(Map<String, Integer> map);
}
