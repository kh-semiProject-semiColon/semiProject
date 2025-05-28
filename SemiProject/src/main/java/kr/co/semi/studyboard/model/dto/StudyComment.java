package kr.co.semi.studyboard.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudyComment {
    
    // STUDY_COMMENT 테이블 컬럼들
    private int studyCommentNo;         // STUDY_COMMENT_NO
    private String studyCommentContent; // STUDY_COMMENT_CONTENT
    private String studyCommentDelFl;   // STUDY_COMMENT_DEL_FL
    private String studyCommentDate;    // STUDY_COMMENT_DATE (String으로 변경)
    private int memberNo;               // MEMBER_NO
    private int studyBoardNo;           // STUDY_BOARD_NO
    private int parentStudyCommentNo2;  // PARENT_STUDY_COMMENT_NO2
    
    // JOIN으로 가져올 추가 정보
    private String memberNickname;      // MEMBER.MEMBER_NICKNAME
    private String studyBoardTitle;     // STUDY_BOARD.STUDY_BOARD_TITLE
}