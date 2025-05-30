package kr.co.semi.studyboard.model.service;

import kr.co.semi.member.model.dto.Member;
import kr.co.semi.studyboard.model.dto.Study;
import kr.co.semi.studyboard.model.dto.StudyBoard;
import kr.co.semi.studyboard.model.dto.StudyComment;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface StudyBoardService {


    
    // 스터디 정보 수정
    int updateStudyInfo(Study study, MultipartFile imageFile);
    
    // 스터디 내규 수정
    boolean updateRule(Study study);
    
    // 스터디 탈퇴
    boolean withdrawMember(Member loginMember);
    
    // 스터디 해체
    boolean deleteStudy(int studyNo);
    
    // 팀장 권한 확인
    boolean isStudyLeader(int memberNo);
    
    // 내 게시글 조회
    Map<String, Object> getMyPosts(int studyNo, int memberNo, int page);
    
    // 현재 멤버 수 조회
    int getCurrentMemberCount(int studyNo);
    
    // 스터디 멤버 목록 조회 - Controller에서 호출하는 메서드 추가
    List<Map<String, Object>> getStudyMembers(int studyNo);

    // 내규 조회 서비스 
	String getStudyrule(Member loginMember);

	// 검색조건 있는 게시글
	Map<String, Object> searchList(Map<String, Object> paramMap, int page);

	/** 해당 스터디 게시글
	 * @param loginMember
	 * @param page
	 * @return
	 */
	Map<String, Object> getStudyInfo(Member loginMember, int cp);

	Study getStudyInfo(Member loginMember);

	StudyBoard studyBoardOne(Map<String, Integer> map);

	int updateStudyBoardCount(int studyBoardNo);

	int studyBoardLike(Map<String, Integer> map);

	

}