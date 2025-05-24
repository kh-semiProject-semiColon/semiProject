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

	public int updateInfo(Member inputMember) {
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
		String newPw2 = paramMap.get("newPw2");

		if (newPw.equals(newPw2)) {
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

	@Override
	public boolean checkPassword(int memberNo, String inputPw) {
		String encryptedPw = mapper.selectPw(memberNo);
		return bcrypt.matches(inputPw, encryptedPw);
	}
}
