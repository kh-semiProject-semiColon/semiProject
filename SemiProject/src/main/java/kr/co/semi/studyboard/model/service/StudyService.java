package kr.co.semi.studyboard.model.service;

import java.util.List;

import kr.co.semi.studyboard.model.dto.Study;

public interface StudyService {

	/** 모든 스터디 조회 
	 * @return
	 */
	List<Study> selectAllStudy();

	/** 팀장인 스터디원 조회
	 * @return
	 */
	List<Study> selectCap();

	/** 스터디 당 스터드원 수 조회
	 * @return
	 */
	List<Study> countMember();

}
