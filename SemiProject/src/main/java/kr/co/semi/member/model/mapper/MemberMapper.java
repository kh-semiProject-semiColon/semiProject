package kr.co.semi.member.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.semi.member.model.dto.Member;

@Mapper
public interface MemberMapper {

	/** 이메일이 일치하면서 탈퇴하지 않은 회원 조회 by 김성원
	 * @param memberEmail
	 * @return
	 */
	Member login(String memberEmail);

	/** 이메일 중복 확인
	 * @param memberEmail
	 * @return
	 */
	int checkEmail(String memberEmail);

	/** 회원가입
	 * @param inputMember
	 * @return
	 */
	int signupInfo(Member inputMember);

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

	/** 입력한 이름, 아이디와 동일한 데이터 조회
	 * @param inputMember
	 * @return
	 */
	int checkNM(Map<String, String> map);

	/** 비밀번호 수정
	 * @param inputMember
	 * @return
	 */
	int changePw(Member inputMember);

	/** 스터디원 정보 조회
	 * @param studyNo
	 * @return
	 */
	List<Member> getStudyMembers(int studyNo);

	List<Member> selectMemberName(int studyNo);

	int result(int memberNo);

	String loginStudyName(int memberNo);



}
