package kr.co.semi.member.model.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import kr.co.semi.common.util.Utility;
import kr.co.semi.member.model.dto.Member;
import kr.co.semi.member.model.mapper.MemberMapper;

@Service
@Transactional(rollbackFor = Exception.class)
@PropertySource("classpath:/config.properties")
@SessionAttributes("loginMember")
public class MemberSerivceImpl implements MemberService {

	@Autowired
	private MemberMapper mapper;
	
	// BCrypt 암호화 객체 의존성 주입(DI) - SecurityConfig 참고
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	
	@Value("${my.profile.web-path}")
	private String profileWebPath;
	
	@Value("${my.profile.folder-path}")
	private String profileFolderPath;
	/**
	 *	로그인 서비스 by 김성원
	 */
	@Override
	public Member login(Member inputMember) {
		
		// 암호화 진행
		
		// bcrypt.encode(문자열) : 문자열을 암호화하여 반환
		// String bcryptPassword = bcrypt.encode(inputMember.getMemberPw());
		// log.debug("bcryptPassword : "+bcryptPassword);
				
		// bcrypt.matches(평문, 암호화): 평문과 암호화가 일치하면 true, 아니면 false 반환
				
		// 1. 이메일이 일치하면서 탈퇴하지 않은 회원 조회
		Member loginMember = mapper.login(inputMember.getMemberEmail()); 
				
		// 2. 만약에 일치하는 이메일이 없어서 조회 결과가 Null 인 경우
		if(loginMember == null) return null;
				
		// 3. 입력받은 비밀번호(평문 : inputMember.getMemberPw() 와
		//		암호화된 비밀번호(loginMember.getMemberPw())
		//		두 비밀번호가 일치하는지 확인
		// 일치하지 않으면
		if(!bcrypt.matches(inputMember.getMemberPw(), loginMember.getMemberPw())) {
			return null;
		}
				
		// 로그인 결과에서 비밀번호 제거
		loginMember.setMemberPw(null);
				
		return loginMember;
	}
	
	// 회원가입시 이메일 유효성 검사
	@Override
	public int checkEmail(String memberEmail) {
		return mapper.checkEmail(memberEmail);
	}
	
	// 회원가입 정보 등록
	@Override
	public int signupInfo(Member inputMember, String[] memberAddress, String profileResult) {
		// 주소가 입력되지 않으면
		// inputMember.getMemberAddress() -> ",,"
		// memberAddress -> [,,]
		
		// 주소가 입력된 겅우
		if(!inputMember.getMemberAddress().equals(",,")){
			
			// String.join("구분자", 배열)
			// -> 배열의 모든 요소 사이에 "구분자"를 추가하여
			// 하나의 문자열로 만들어 반환하는 메서드
			String address = String.join("^^^", memberAddress);
			// [12345, 서울시 중구 남대문로, 3층, E강의장]
			// -> 12345^^^서울시 중구 남대문로^^^3층, E강의장
			
			// 구분자로 "^^^"를 쓴 이유 : 주소나 상세주소에 안 쓸 것 같은 특수문자로 작성
			// -> 나중에 마이페이지에서 주소 부분 수정시
			// -> DB에 저장된 기존 주소를 화면에 출력해줘야한다
			// -> 다시 3분할 해야할 때 구분자로 "^^^" 사용
			// -> 구분자가 기본형태인 ","로 작성되어 있을 경우
			// -> 주소, 상세주소에 ","가 포함되어 있으면
			// -> 분할이 제대로 이루어지지 않을 수 있다
			
			// inputMember.memberAddress로 합쳐준 주소 세팅
			inputMember.setMemberAddress(address);
			
		}else { // 주소가 입력되지 않은 경우
			inputMember.setMemberAddress(null);
		}

		// 비밀번호 암호화 진행
		
		// inputMember 안의 memeberPw -> 평문
		// 비밀번호를 암호화하여 inputMember 세팅
		
		String encPw = bcrypt.encode(inputMember.getMemberPw());
		inputMember.setMemberPw(encPw);
		inputMember.setProfileImg(profileResult);

		// 회원 가입 mapper 메서드 호출
		return mapper.signupInfo(inputMember);
	}
	
	// 닉네임 중복 검사
	@Override
	public int checkNickname(String memberNickname) {
		return mapper.checkNickname(memberNickname);
	}
	
	// ID찾기 일치하는 이름, 전화번호 조회
	@Override
	public int checkName(Member inputMember) {
		return mapper.checkName(inputMember);
	}
	
	// 아이디 얻어오기
	@Override
	public String getId(Member inputMember) {
		return mapper.getId(inputMember);
	}
	
	// 입력한 이름, 아이디와 동일한 데이터 조회
	@Override
	public int checkNM(Map<String, String> map) {
		return mapper.checkNM(map);
	}

	// 비밀번호 수정
	@Override
	public int changePw(Member inputMember) {
		
		String encPw = bcrypt.encode(inputMember.getMemberPw());
		inputMember.setMemberPw(encPw);
		
		return mapper.changePw(inputMember);
	}
	
	@Override
	public String profile(MultipartFile profileImg, Member inputMember) throws Exception{
		// 프로필 이미지 경로
				String updatePath = null;
				String finalPath = null;
				
				// 변경명 저장
				String rename = null;
				
				// 업로드한 이미지가 있을 경우
				// - 있을 경우 : 경로 조합 (클라이언트 접근 경로 + 리네임파일명)
				if(!profileImg.isEmpty()) {
					
					// 1. 파일명 변경
					rename = Utility.fileRename(profileImg.getOriginalFilename());
					
					// 2. /myPage/profile/변경된 파일명
					updatePath = profileFolderPath + rename;
					finalPath = profileWebPath + rename;
					profileImg.transferTo(new File(updatePath));
				}
				
				

				return finalPath;
	}

	@Override
	public List<Member> getStudyMembers(int studyNo) {
		return mapper.getStudyMembers(studyNo);
	}
}
