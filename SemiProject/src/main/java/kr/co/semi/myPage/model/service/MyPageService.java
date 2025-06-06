package kr.co.semi.myPage.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import kr.co.semi.board.model.dto.Board;
import kr.co.semi.board.model.dto.Pagination;
import kr.co.semi.member.model.dto.Member;

public interface MyPageService {

	// memberIntroduce, memberMajor 가져오는 메서드
	Member selectMember(int memberNo);

	// 회원정보 update문 메서드
	public int updateInfo(Member inputMember, Member loginMember, MultipartFile profileImg) throws Exception;

	// 비밀번호 조회하러 가는 길
	boolean checkPassword(int memberNo, String inputPw);

	// 비밀번호 변경하러 가는 메서드
	int changePw(Map<String, String> paramMap, int memberNo);

	// 회원 탈퇴 메서드
	int deleteMember(int memberNo);

	// 페이지네이션 포함
	Map<String, Object> selectBoardWithPaging(int memberNo, int cp);

	// 페이지네이션 포함 댓글
	Map<String, Object> selectCommentWithPaging(int memberNo, int commentCp);

}
