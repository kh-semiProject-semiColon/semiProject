package kr.co.semi.board.model.service;

import java.util.List;
import java.util.Map;

import kr.co.semi.board.model.dto.Board;
import kr.co.semi.board.model.dto.HireInfo;
import kr.co.semi.member.model.dto.Member;
import kr.co.semi.studyboard.model.dto.Study;

public interface HireBoardService {
	
	// 구인 게시글 조회
	Map<String, Object> showHireBoard(int cp);

	// 구인 게시글 검색
	Map<String, Object> searchList(Map<String, Object> paramMap, int cp);

	// 내 스터디 조회
	List<Study> showStudySelect(int memberNo);

	// 구인 게시글 작성
	int writeHireBoardInsert(HireInfo inputHire);

	// 구인 게시글 상세 조회
	HireInfo selectOne(Map<String, Integer> map);

	// 조회수 설정 (쿠키)
	int updateHireReadCount(int hireNo);

	// 구인 스터디 조회
	Study selectStudyNo(int studyNo);

	// 게시글 수정
	int hireUpdate(HireInfo inputHire);

	// 게시글 삭제
	int hireDelete(Map<String, Integer> map);

	// 스터디 번호 구하기
	int getStudyNo(int hireNo);

	// 모집인원 구하기
	int hireCount(int studyNo);

	// 스터디에 멤버 초대하기
	int memberInvite(Map<String, Integer> map);

	// 모달창 열기
	Member getResumeByMemberNo(int memberNo);

	// 이전에 멤버 초대 했는지 검사
	int invitation(Map<String, Integer> map);


	

}
