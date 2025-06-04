package kr.co.semi.studyboard.model.dto;

import java.util.List;

import kr.co.semi.board.model.dto.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Study {
    
    // STUDY 테이블 컬럼
    private int studyNo;           // STUDY_NO
    private String studyName;      // STUDY_NAME
    private String studyDelFl;     // STUDY_DEL_FL
    private int studyMaxCount;     // STUDY_MAX_COUNT
    private String studyDate;      // STUDY_DATE (String으로 변경)
    private int studyType;         // STUDY_TYPE
    private int studyPeriod;       // STUDY_PERIOD
    private String studyMainImg;   // STUDY_MAIN_IMG
    
    // STUDY_RULE 테이블 컬럼 (JOIN)
    private String ruleContent;    // RULE_CONTENT
    
    // STUDY_MEMBER 테이블 컬럼 (JOIN)
    private String studyCap;       // STUDY_CAP (팀장 여부 Y/N)
    private String MemberCount;    // 스케줄은 MemberCount로 받아옴
    private int memberNo;          // MEMBER_NO
    
    // 추가 필드들 (계산 또는 JOIN으로 가져올 데이터)
    private String memberNickname; // MEMBER.MEMBER_NICKNAME
    private int currentMemberCount; // COUNT로 계산
    private String memberJoinDate; // 실제로는 STUDY_MEMBER에 JOIN_DATE 컬럼이 없음 (String으로 변경)
    
	private int commentCount;				// 댓글 개수
	private int likeCount;					// 좋아요 개수
	private String profileImg;				// 프로필 이미지
	private List<StudyComment> commentList;		// 댓글 목록
	private int likeCheck;					// 좋아요 여부
	
    
    // ========================================
    // 🔥 이 메서드가 누락되어 있었습니다!
    // ========================================
    /**
     * 팀장 여부 확인 메서드
     * @return studyCap이 'Y'이면 true, 아니면 false
     */
    public boolean isLeader() {
        return "Y".equals(this.studyCap);
    }
    
    // 스터디 타입 텍스트 변환
    public String getStudyTypeText() {
        switch(this.studyType) {
            case 1: return "백엔드";
            case 2: return "복습";
            case 3: return "문제풀이";
            case 4: return "자격증";
            case 5: return "프로젝트";
            default: return "기타";
        }
    }
    
    // 스터디 기간 텍스트 변환
    public String getStudyPeriodText() {
        switch(this.studyPeriod) {
            case 0: return "종강까지";
            case 1: return "1개월";
            case 2: return "2개월";
            case 3: return "3개월";
            case 4: return "4개월";
            case 5: return "5개월";
            case 6: return "6개월";
            default: return "미정";
        }
    }


}