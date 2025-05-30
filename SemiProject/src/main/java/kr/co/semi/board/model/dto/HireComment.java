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
}
