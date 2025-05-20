package kr.co.semi.member.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member {
	
	private int memberNo;
	private int studyNo;
	private String memberEmail;
	private String memberNickname;
	private String memberTel;
	private String memberAddress;
	private String profileImg;
	private String memberDelFl;
	private String authority;
	private String enrollDate;
	private String memberMajor;
	private String memberIntroduce;
	private String memberName;
}
