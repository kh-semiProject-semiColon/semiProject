package kr.co.semi.studyboard.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudyBoard {

    // STUDY_BOARD 테이블 컬럼들
    private int studyBoardNo;           // STUDY_BOARD_NO
    private String studyBoardTitle;     // STUDY_BOARD_TITLE
    private int memberNo;               // MEMBER_NO
    private int studyNo;                // STUDY_NO
    private String studyBoardWriteDate;  // STUDY_BOARD_WRITE_DATE (String으로 변경)
    private String studyBoardUpdateDate; // STUDY_BOARD_UPDATE_DATE (String으로 변경)
    private String studyBoardContent;   // STUDY_BOARD_CONTENT
    private int readCount;              // READ_COUNT
    private String studyBoardDelFl;     // STUDY_BOARD_DEL_FL

    // JOIN으로 가져올 추가 정보
    private String memberNickname;      // MEMBER.MEMBER_NICKNAME
    private int likeCount;              // 좋아요 기능이 있다면 별도 계산
    
	private int commentCount;				// 댓글 개수
	private String profileImg;				// 프로필 이미지
	private List<StudyComment> commentList;		// 댓글 목록
	private int likeCheck;					// 좋아요 여부
}