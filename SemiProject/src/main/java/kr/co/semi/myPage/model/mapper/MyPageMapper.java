package kr.co.semi.myPage.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.semi.board.model.dto.Board;
import kr.co.semi.member.model.dto.Member;

@Mapper
public interface MyPageMapper {


	// 자기소개, 전공여부, 프로필 사진 들고 오는 메서드
	Member selectMember(int memberNo);

	// 업데이트 해주는 구문
	int updateInfo(Member inputMember);

	// 원래 비밀번호 조회하러 가는 길
	String selectPw(int memberNo);

	// 비밀번호 변경하러 가는 메서드
	int changePw(Map<String, String> paramMap);

	// 회원탈퇴 메서드
	int deleteMember(int memberNo);

	// 보드 리스트 들고오는 메서드
	List<Board> selectBoard(int memberNo);
	


}
