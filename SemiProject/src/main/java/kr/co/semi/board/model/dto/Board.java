package kr.co.semi.board.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board {

	private int boardNo;						// 게시글 번호
	private int readCount; 				// 게시글 조회수
	private String boardDelFl;				// 게시글 삭제 여부
	private String boardWriteDate;				// 게시글 작성 날짜
	private String boardUpdateDate;				// 게시글 수정 날짜
	private String boardContent;				// 게시글 내용
	private String boardTitle;				// 게시글 제목
	private int boardCode;				// 게시판 번호
	
	private int memberNo;					// 회원고유번호
	private String memberNickname;			// 회원 닉네임
	private int commentCount;				// 댓글 개수
	private int likeCount;					// 좋아요 개수
	
	
}
