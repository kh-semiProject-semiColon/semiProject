package kr.co.semi.main.service;

import kr.co.semi.studyboard.model.dto.Study;

public interface MainService {

	/** 스터디 생성 서비스
	 * @param study
	 * @param memberNo 
	 * @return
	 */
	int studyCreation(Study study, int memberNo); 

} 