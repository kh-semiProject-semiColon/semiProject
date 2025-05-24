package kr.co.semi.myPage.model.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	@Override
	public Member selectMember(int memberNo) {
		return (Member) mapper.selectMember(memberNo);
	}

	@Override
	public int updateInfo(Member inputMember) {

		// 입력된 주소가 있을 경우
		if (!inputMember.getMemberAddress().equals(",,")) {

			String address = String.join("^^^", inputMember.getMemberAddress());
			inputMember.setMemberAddress(address);

		} else {
			// 없을 경우
			inputMember.setMemberAddress(null);
		}

		return mapper.updateInfo(inputMember);
	}

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
		String newPw2 = paramMap.get("newPw");

		if (newPw.equals(newPw2)) {
			// 새 비밀번호를 암호화(bcrypt.encode(평문) > 암호화된 비밀번호 반환)
			String encPw = bcrypt.encode(paramMap.get("newPw"));

			// DB에 업데이트
			// SQL 전달 해야하는 데이터 2개 (암호화한 새 비번 encPw, 회원번호 memberNo)
			// -> mapper에 전달할 수 있는 전달인자는 단 1개..
			// -> 묶어서 전달 (paramMap 재활용)
			paramMap.put("encPw", encPw);
			paramMap.put("memberNo", memberNo + ""); // 1 + "" => 문자열

		}

		return mapper.changePw(paramMap);
	}

	@Override
	public boolean checkPassword(int memberNo, String inputPw) {
	    String encryptedPw = mapper.selectPw(memberNo);
	    return bcrypt.matches(inputPw, encryptedPw);
	}
}
