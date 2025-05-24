package kr.co.semi.myPage.model.service;

import java.util.Map;

import kr.co.semi.member.model.dto.Member;

public interface MyPageService {

	// memberIntroduce, memberMajor 가져오는 메서드
	Member selectMember(int memberNo);

	// 회원정보 update문 메서드
	int updateInfo(Member inputMember);

	// 비밀번호 조회하러 가는 길
	boolean checkPassword(int memberNo, String inputPw);

	// 비밀번호 변경하러 가는 메서드
	int changePw(Map<String, String> paramMap, int memberNo);


}
