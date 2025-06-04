package kr.co.semi.board.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HireComment {

	private int hireCommentNo;			// 댓글 번호
	private String hireCommentContent;	// 댓글 내용
	private String hireCommentDelFl;	// 댓글 삭제 여부
	private String hireCommentDate;		// 댓글 작성일
	private String hireParentCommentNo;	// 부모 댓글 번호
	
	private int hireNo;					// 게시글 번호
	
	private int memberNo;				// 회원 고유 번호
	private String memberNickname;
	private String profileImg;
	
	private int StudyNo;				// 스터디 번호
	
	private int studyCount; 			// 스터디 카운트
	private int studyMaxCount;			// 스터디 최대 인원
	private int currentCount;			// 스터디 현재 인원
}
