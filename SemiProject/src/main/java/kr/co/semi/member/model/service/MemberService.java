package kr.co.semi.member.model.service;

import kr.co.semi.member.model.dto.Member;

public interface MemberService {

	/** 로그인 서비스 by 김성원
	 * @param inputMember
	 * @return
	 */
	Member login(Member inputMember);

	/** 이메일 중복 검사
	 * @param memberEmail
	 * @return
	 */
	int checkEmail(String memberEmail);

	/** 회원가입 서비스
	 * @param inputMember
	 * @param memberAddress
	 * @return
	 */
	int signupInfo(Member inputMember, String[] memberAddress);

	/** 닉네임 중복검사
	 * @param memberNickname
	 * @return
	 */
	int checkNickname(String memberNickname);

}
