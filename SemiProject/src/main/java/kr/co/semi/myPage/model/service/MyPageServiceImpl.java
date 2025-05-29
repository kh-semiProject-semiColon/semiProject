package kr.co.semi.myPage.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.co.semi.board.model.dto.Board;
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

	// 자기 페이지 수정하는 메서드
	@Override
	public int updateInfo(Member inputMember, MultipartFile profileImg) {
		
		// 프로필 이미지 경로
		String updatePath = null;
		
		// 이미지 파일명 변경
		String rename = null;
		
		// 업로드한 이미지가 있을 경우
		// - 있을 경우 : 경로 조합 (클라이언트 접근 경로 + 리네임파일명)
		if(!profileImg.isEmpty()) {
			// 1. 파일명 변경
			rename = Utility.fileRename(profileImg.getOriginalFilename());
			
			// 2. /myPage/profile/변경된 파일명
			updatePath = profileWebPath + rename;
		}
		
		// 수정된 프로필 이미지 경로 + 회원 번호를 저장할 DTO 객체

		// 수정된 프로필 이미지 경로 + 회원 번호를 저장할 DTO 객체
//		Member member = Member.builder().memberNo(loginMember.getMemberNo()).profileImg(updatePath).build();
//
//		int result = mapper.profile(member);
//
//		if (result > 0) {
//
//			// 프로필 이미지를 없애는 update 를 한 경우
//			// -> 업로드한 이미지가 있을 경우
//			if (!profileImg.isEmpty()) {
//				// 파일을 서버에 저장
//				profileImg.transferTo(new File(profileFolderPath + rename));
//				// C:/uploadFiles/profile/변경한 이름
//			}
//
//			// 세션에 저장된 loginMember의 프로필 이미지 경로를
//			// DB와 동기화
//			loginMember.setProfileImg(updatePath);
//
//		}
		
		
		String rawAddress = inputMember.getMemberAddress();

		if (rawAddress != null && !rawAddress.trim().isEmpty()) {

			// 쉼표로 된 형식이 들어왔다면 분리해서 처리
			String[] parts = rawAddress.split(",");

			// 최대 3개의 주소 파트를 안전하게 추출
			String postcode = parts.length > 0 ? parts[0].trim() : "";
			String address = parts.length > 1 ? parts[1].trim() : "";
			String detailAddress = parts.length > 2 ? parts[2].trim() : "";

			// `^^^` 구분자로 다시 join
			String combined = String.join("^^^", postcode != null ? postcode : "", address != null ? address : "",
					detailAddress != null ? detailAddress : "");

			inputMember.setMemberAddress(combined);
		} else {
			inputMember.setMemberAddress(null);
		}

		return mapper.updateInfo(inputMember);
	}

	/**
	 * 게시글을 조회, 상세조회하는 메서드
	 *
	 */
	@Override
	public List<Board> selectBoard(int memberNo) {
		return mapper.selectBoard(memberNo);
	}

	/**
	 * 댓글을 조회, 상세조회하는 메서드
	 *
	 */
	@Override
	public List<Map<String, String>> selectComment(int memberNo) {
		return mapper.selectComment(memberNo);
	}

	/**
	 * 비밀번호 변경하는 메서드
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
