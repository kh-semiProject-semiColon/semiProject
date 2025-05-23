package kr.co.semi.myPage.model.service;

import java.util.List;
import java.util.Map;

import kr.co.semi.member.model.dto.Member;

public interface MyPageService {

	// memberIntroduce, memberMajor 가져오는 메서드
    Member selectMember(int memberNo);

    // update문 메서드
	int updateInfo(Member inputMember);
    
    


}
