package kr.co.semi.myPage.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.semi.member.model.dto.Member;
import kr.co.semi.myPage.model.mapper.MyPageMapper;

@Service
@Transactional(rollbackFor = Exception.class)
@PropertySource("classpath:/config.properties")
public class MyPageServiceImpl implements MyPageService {

	@Autowired
	private MyPageMapper mapper;

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

}
