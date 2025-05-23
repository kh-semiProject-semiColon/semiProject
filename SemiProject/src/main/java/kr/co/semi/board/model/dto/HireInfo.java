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
public class HireInfo {

	private int hireNo;						// 게시글 번호
	private int hireReadCount; 				// 게시글 조회수
	private String hireDelFl;				// 게시글 삭제 여부
	private String hireDate;				// 게시글 작성 날짜
	private String hireUpdate;				// 게시글 수정 날짜
	private String hireContent;				// 게시글 내용
	private String dayCanJoin;				// 요일 정보
	private int hireCount;					// 현재 모집 인원
	private String hireTitle;				// 게시글 제목
		
	private int memberNo;					// 회원고유번호
	private String  memberNickName;			// 회원 닉네임 (member 테이블 조인)
	private String profileImg;				// 프로필 이미지
	
	private int studyNo;					// 스터디 고유 번호
	
	private int commentCount;				// 댓글수
	
		
	private String studyMainImg;			// 스터디 메인 이미지(게시판 썸네일)
	
	private List<HireComment> commentList;	//
	
}
