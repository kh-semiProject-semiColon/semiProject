package kr.co.semi.board.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

		private int commentNo;
		private String commentContent;
		private String commentWrittenDate;
		private String commentDelFl;
		private int boardNo;
		private int memberNo;
		private int parentCommentNo;
		
		// 댓글 조회 시 회원 프로필, 닉네임
		private String memberNickname;
		private String profileImg;
	
}
