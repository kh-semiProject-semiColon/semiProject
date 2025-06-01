package kr.co.semi.main.service;

import kr.co.semi.board.model.dto.Announce;
import kr.co.semi.board.model.dto.HireInfo;
import kr.co.semi.studyboard.model.dto.Study;

public interface MainService {

	/** 스터디 생성 서비스
	 * @param study
	 * @param memberNo 
	 * @return
	 */
	int studyCreation(Study study, int memberNo);

	/** 스터디명 중복 검사
	 * @param studyName
	 * @return
	 */
	int studyNameConfirm(String studyName);

	/** 최신 구인게시글을 조회 서비스
	 * @return
	 */
	HireInfo latedstPost();

	/** 최신 공지사항 글 조회 서비스
	 * @return
	 */
	Announce latestAnouncement(); 

} 