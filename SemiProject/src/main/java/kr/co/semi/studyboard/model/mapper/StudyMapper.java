package kr.co.semi.studyboard.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import kr.co.semi.studyboard.model.dto.Study;

@Mapper
public interface StudyMapper {


	/** 팀장인 스터디원 조회
	 * @return
	 */
	List<Study> selectCap();

	/** 삭제되지 않은 스터디 조회 개수
	 * @return
	 */
	int getStudyCount();

	/** 모든 스터디 조회
	 * @param rowBounds
	 * @return
	 */
	List<Study> selectAllStudy(RowBounds rowBounds);

	/** 검색 조건에 부합하면서 삭제되지 않은 스터디 수
	 * @param paramMap
	 * @return
	 */
	int getSearchCount(Map<String, Object> paramMap);

	/** 검색조건에 부합하는 스터디 조회
	 * @param paramMap
	 * @param rowBounds
	 * @return
	 */
	List<Study> selectSearchList(Map<String, Object> paramMap, RowBounds rowBounds);

	List<Study> selectMainStudy();




}
