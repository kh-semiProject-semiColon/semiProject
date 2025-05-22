package kr.co.semi.myPage.model.service;

import java.util.List;
import java.util.Map;

import kr.co.semi.member.model.dto.Member;

public interface MyPageService {

	Map<Member, String> selectMember(int memberNo);

}
