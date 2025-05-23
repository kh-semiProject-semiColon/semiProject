package kr.co.semi.member.model.service;

import java.util.Map;

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

	/** 아이디 찾기 이름 조회
	 * @param memberName
	 * @return
	 */
	int checkName(Member inputMember);

	/** 아이디 얻어오기
	 * @param memberName
	 * @param memberTel
	 * @return
	 */
	String getId(Member inputMember);

	/**이름 이메일 동일성 검사
	 * @param map
	 * @return
	 */
	int checkNM(Map<String, String> map);

	/** 비밀번호 수정
	 * @param inputMember
	 * @return
	 */
	int changePw(Member inputMember);

}
