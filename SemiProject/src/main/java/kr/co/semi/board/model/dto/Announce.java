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
public class Announce {

	private int announceNo;						// 게시글 번호
	private int announceCount; 				// 게시글 조회수
	private String announceDelFl;				// 게시글 삭제 여부
	private String announceDate;				// 게시글 작성 날짜
	private String announceUpdateDate;				// 게시글 수정 날짜
	private String announceContent;				// 게시글 내용
	private String announceName;				// 게시글 제목
		
	private int memberNo;					// 회원고유번호
	private String memberNickname;			// 회원 닉네임
	
	
}
