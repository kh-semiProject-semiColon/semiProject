package kr.co.semi.member.model.service;

import kr.co.semi.member.model.dto.Member;

public interface MemberService {

	/** 로그인 서비스 by 김성원
	 * @param inputMember
	 * @return
	 */
	Member login(Member inputMember);

}
