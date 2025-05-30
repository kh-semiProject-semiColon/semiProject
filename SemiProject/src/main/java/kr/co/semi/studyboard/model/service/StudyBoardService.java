package kr.co.semi.studyboard.model.service;

import kr.co.semi.member.model.dto.Member;
import kr.co.semi.studyboard.model.dto.Study;
import kr.co.semi.studyboard.model.dto.StudyBoard;
import kr.co.semi.studyboard.model.dto.StudyComment;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface StudyBoardService {

    // 스터디 정보 조회 -- 완
    Study getStudyInfo(Member loginMember);
    
    // 스터디 정보 수정
    int updateStudyInfo(Study study, MultipartFile imageFile);
    
    // 스터디 내규 수정
    boolean updateRule(Study study);
    
    // 스터디 탈퇴
    boolean withdrawMember(Member loginMember);
    
    // 팀장 권한 확인
    boolean isStudyLeader(int memberNo);
    
    // 내 게시글 조회
    Map<String, Object> getMyPosts(int studyNo, int memberNo, int page);
    
    // 현재 멤버 수 조회
    int getCurrentMemberCount(int studyNo);
    
    // 스터디 멤버 목록 조회 - Controller에서 호출하는 메서드 추가
    List<Member> getStudyMembers(int studyNo);
  

    // 내규 조회 서비스 
	String getStudyrule(Member loginMember);

	// 스터디 멤버인지 확인하는 메서드
	boolean isStudyMember(Member member);

	//팀장권한 위임 및 탈퇴처리
	boolean transferLeadershipAndWithdraw(Member member, Member loginMember);

	// 스터디 탈퇴하고 해체
	int studyDelete(Member loginMember);


	

}