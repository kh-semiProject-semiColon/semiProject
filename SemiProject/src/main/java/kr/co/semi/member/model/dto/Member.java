package kr.co.semi.member.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Member DTO 생성
 * by 김성원
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member {
	
	private int memberNo;
	private int studyNo;
	private String memberEmail;
	private String memberPw;
	private String memberNickname;
	private String memberTel;
	private String memberAddress;
	private String profileImg;
	private String memberDelFl;
	private int authority;
	private String enrollDate;
	private String memberMajor;
	private String memberIntroduce;
	private String memberName;
}

