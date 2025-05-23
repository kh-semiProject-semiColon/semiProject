package kr.co.semi.myPage.model.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.semi.member.model.dto.Member;

@Mapper
public interface MyPageMapper {


	// 자기소개, 전공여부, 프로필 사진 들고 오는 메서드
	Member selectMember(int memberNo);

	// 업데이트 해주는 구문
	int updateInfo(Member inputMember);
	


}
