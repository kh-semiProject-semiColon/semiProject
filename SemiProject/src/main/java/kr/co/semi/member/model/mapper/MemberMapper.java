package kr.co.semi.member.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import kr.co.semi.member.model.dto.Member;

@Mapper
public interface MemberMapper {

	/** 이메일이 일치하면서 탈퇴하지 않은 회원 조회 by 김성원
	 * @param memberEmail
	 * @return
	 */
	Member login(String memberEmail);

}
