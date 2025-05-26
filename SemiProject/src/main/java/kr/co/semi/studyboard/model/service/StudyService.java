package kr.co.semi.studyboard.model.service;

import java.util.List;
import java.util.Map;

import kr.co.semi.studyboard.model.dto.Study;

public interface StudyService {

	/** 모든 스터디 조회 
	 * @param cp 
	 * @return
	 */
	Map<String, Object> selectAllStudy(int cp);

	/** 팀장인 스터디원 조회
	 * @return
	 */
	List<Study> selectCap();

	/** 검색한 스터디 조회
	 * @param paramMap
	 * @param cp
	 * @return
	 */
	Map<String, Object> searchList(Map<String, Object> paramMap, int cp);

	/** 메인페이지 미니 현황에서 사용할 스터디 불러오기
	 * @return
	 */
	List<Study> selectMainStudy();


}
