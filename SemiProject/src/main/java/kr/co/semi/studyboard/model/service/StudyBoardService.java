package kr.co.semi.studyboard.model.service;

import kr.co.semi.member.model.dto.Member;
import kr.co.semi.studyboard.model.dto.Study;
import kr.co.semi.studyboard.model.dto.StudyBoard;

import java.util.List;

/**
 * 🎯 StudyBoardService
 * 📌 게시판 기능에 대한 인터페이스 정의
 * 🔗 Controller에서 호출되며, 구현체는 StudyBoardServiceImpl
 */
public interface StudyBoardService {


	/**  모든 스터디 조회
	 * @param loginMember
	 * @return
	 */
	Study studyInfo(Member loginMember);
}
