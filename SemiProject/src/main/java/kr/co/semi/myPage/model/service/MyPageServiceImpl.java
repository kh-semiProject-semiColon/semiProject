package kr.co.semi.myPage.model.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.co.semi.board.model.dto.Board;
import kr.co.semi.board.model.dto.Pagination;
import kr.co.semi.common.util.Utility;
import kr.co.semi.member.model.dto.Member;
import kr.co.semi.myPage.model.mapper.MyPageMapper;

@Service
@Transactional(rollbackFor = Exception.class)
@PropertySource("classpath:/config.properties")
public class MyPageServiceImpl implements MyPageService {

	@Autowired // 메퍼에 의존성 주입
	private MyPageMapper mapper;

	@Autowired // 현재 비밀번호와 비교하기 위한 의존성 주입
	private BCryptPasswordEncoder bcrypt;

	@Value("${my.profile.web-path}")
	private String profileWebPath; // /myPage/profile/

	@Value("${my.profile.folder-path}")
	private String profileFolderPath; // C:/uploadFiles/profile/

	// 전공자 여부, 자기소개 들고오는 메서드
	@Override
	public Member selectMember(int memberNo) {
		return (Member) mapper.selectMember(memberNo);
	}

	@Override
	public int updateInfo(Member inputMember, Member loginMember, MultipartFile profileImg) throws Exception {
		
		int result = 0;
		
		String updatePath = null;
		
		String rename = null;

		// 1. 주소 처리
		String rawAddress = inputMember.getMemberAddress();
		if (rawAddress != null && !rawAddress.trim().isEmpty()) {
			String[] parts = rawAddress.split(",");
			String postcode = parts.length > 0 ? parts[0].trim() : "";
			String address = parts.length > 1 ? parts[1].trim() : "";
			String detailAddress = parts.length > 2 ? parts[2].trim() : "";

			String combined = String.join("^^^", postcode != null ? postcode : "", address != null ? address : "",
					detailAddress != null ? detailAddress : "");

			inputMember.setMemberAddress(combined);
		} else {
			inputMember.setMemberAddress(null);
		}

		// 2. 기본 회원 정보 업데이트
		result = mapper.updateInfo(inputMember);

		if (result > 0) {

			// 3-1. 프로필 이미지 삭제 요청 (기본 이미지로 변경 요청)
			if (inputMember.getProfileImg() != null && inputMember.getProfileImg().contains("default-profile.png")) {
				Member profileMember = Member.builder().memberNo(loginMember.getMemberNo())
						.profileImg("/images/default-profile.png").build();

				int profileResult = mapper.profile(profileMember);
				if (profileResult > 0) {
					loginMember.setProfileImg(profileMember.getProfileImg());
				} else {
					result = 0; // 실패 시 전체 롤백
				}
			}

			// 3-2. 프로필 이미지 새로 업로드한 경우
			else if (profileImg != null && !profileImg.isEmpty()) {
				rename = Utility.fileRename(profileImg.getOriginalFilename());
				updatePath = profileWebPath + rename;

				Member profileMember = Member.builder().memberNo(loginMember.getMemberNo()).profileImg(updatePath)
						.build();

				int profileResult = mapper.profile(profileMember);
				if (profileResult > 0) {
					profileImg.transferTo(new File(profileFolderPath + rename));
					loginMember.setProfileImg(updatePath);
				} else {
					result = 0;
				}
			}

			// else: 아무 것도 안 바꾸는 경우는 무시
		}

		return result;
	}

	// 기존의 selectBoard(int memberNo) 대신 페이지네이션 버전
	public Map<String, Object> selectBoardWithPaging(int memberNo, int cp) {
		// 해당 회원의 총 게시글 수 조회
		int listCount = mapper.getBoardCountByMember(memberNo);

		// 페이지네이션 객체 생성
		Pagination pagination = new Pagination(cp, listCount);

		// RowBounds로 페이징 처리
		int limit = pagination.getLimit();
		int offset = (cp - 1) * limit;
		RowBounds rowBounds = new RowBounds(offset, limit);

		// 페이징된 게시글 목록 조회
		List<Board> boardList = mapper.selectBoardWithPaging(memberNo, rowBounds);

		// 결과 반환
		Map<String, Object> map = new HashMap<>();
		map.put("boardList", boardList);
		map.put("pagination", pagination);

		return map;
	}

	// 댓글 총 개수 조회 + 페이징 처리
	public Map<String, Object> selectCommentWithPaging(int memberNo, int cp) {
		// 해당 회원의 총 댓글 수 조회
		int listCount = mapper.getCommentCountByMember(memberNo);

		// 페이지네이션 객체 생성
		Pagination pagination = new Pagination(cp, listCount);

		// RowBounds로 페이징 처리
		int limit = pagination.getLimit();
		int offset = (cp - 1) * limit;
		RowBounds rowBounds = new RowBounds(offset, limit);

		// 페이징된 댓글 목록 조회
		List<Map<String, String>> commentList = mapper.selectCommentWithPaging(memberNo, rowBounds);

		// 결과 반환
		Map<String, Object> map = new HashMap<>();
		map.put("commentList", commentList);
		map.put("pagination", pagination);

		return map;
	}

	/**
	 * 비밀번호 변경
	 *
	 */
	@Override
	public int changePw(Map<String, String> paramMap, int memberNo) {

		// 원래 있는 비밀번호와 일치하는 지 조회하기
		String originPw = mapper.selectPw(memberNo);

		// 하지만 입력받은 현재 비밀번호는 pass01!
		// 암호화된 비밀번호와 비교해야 한다! 반드시!
		if (!bcrypt.matches(paramMap.get("originPw"), originPw)) {
			return 0;
		}

		// 2. 같을 경우 비밀번호 확인과도 비교
		String newPw = paramMap.get("newPw");
		String newPwConfirm = paramMap.get("newPwConfirm");

		if (newPw.equals(newPwConfirm)) {
			// 기존 비밀번호와 새 비밀번호 평문-암호화 비교
			if (bcrypt.matches(newPw, originPw)) {
				return 0; // 기존 비밀번호와 같으면 변경 안 함
			}

			String encPw = bcrypt.encode(newPw);

			paramMap.put("encPw", encPw);
			paramMap.put("memberNo", String.valueOf(memberNo));

		}
		return mapper.changePw(paramMap);
	}

	/**
	 * 회원탈퇴전 비밀번호가 일치하는지 확인하는 메서드
	 *
	 */
	@Override
	public boolean checkPassword(int memberNo, String inputPw) {
		String encryptedPw = mapper.selectPw(memberNo);
		return bcrypt.matches(inputPw, encryptedPw);
	}

	/**
	 * 회원 삭제하러 가는 메서즈
	 *
	 */
	@Override
	public int deleteMember(int memberNo) {
		return mapper.deleteMember(memberNo);
	}

}
