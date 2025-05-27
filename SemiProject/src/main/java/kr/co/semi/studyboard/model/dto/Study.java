package kr.co.semi.studyboard.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Study {
    
    // 기본 스터디 정보 (STUDY 테이블)
    private int studyNo;
    private String studyName;
    private String studyDelFl;
    private int studyMaxCount;
    private String studyDate;        // Date 타입으로 변경 권장
    private int studyType;
    private int studyPeriod;
    private String studyMainImg;
    
    // 스터디 규칙 (STUDY_RULE 테이블)
    private String ruleContent;
    
    // 스터디 멤버 관련 (STUDY_MEMBER 테이블)
    private String studyCap;       // 팀장 여부
    private int memberNo;          // 현재 사용자의 회원번호
    private int memberCount;       // 현재 스터디 참여 인원수
    
    // 추가로 고려할 수 있는 필드들
    private String memberNickname; // 팀장 닉네임 (조인 시 사용)
    private boolean isLeader;      // 현재 사용자가 팀장인지 여부
    private boolean isMember;      // 현재 사용자가 멤버인지 여부
}
