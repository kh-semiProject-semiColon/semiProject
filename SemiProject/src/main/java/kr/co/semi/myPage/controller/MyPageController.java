package kr.co.semi.myPage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import kr.co.semi.member.model.dto.Member;
import kr.co.semi.myPage.model.service.MyPageService;
import lombok.extern.slf4j.Slf4j;

// 로그인 해야 들어올 수 있으니까 sessionAttributes를 가져왔습니다.
@SessionAttributes({"loginMember"})
@Controller
@RequestMapping("myPage")
@Slf4j
public class MyPageController {

	// 회원가입 시 적었던 내용들을 조회할 수 있게 하는 메서드
	// 
	@RequestMapping("info")
	public String info(@SessionAttribute("loginMember") Member loginMember,
								Model model) {
		
		String memberName = loginMember.getMemberName();
		String profileImg = loginMember.getProfileImg();
		String memberNickname = loginMember.getMemberNickname();
		String memberTel = loginMember.getMemberTel();
		String memberEmail = loginMember.getMemberEmail();
		String memberMajor= loginMember.getMemberMajor();
		String memberAddress = loginMember.getMemberAddress();
		String memberIntroduce = loginMember.getMemberIntroduce();
		
		if(memberAddress != null) {
			String[] arr = memberAddress.split("\\^\\^\\^");
			
			model.addAttribute("postcode", arr[0]);
			model.addAttribute("address", arr[1]);
			// 테이블 안에 주소컬럼은 notnull값이 아니므로 3개의 부분이 모두 존재하지 않을 가능성 있음
			// 따라서 조건을 나누어, null이 아닌경우, 두번째 배열(arr[1])까지만 존재할 경우, 세번재 배열 모두 존재할 경우를
			// 고려해야 함
			model.addAttribute("detailAddress", arr[2]);
		}
		return "myPage/myPage-info";
	}
	
	@GetMapping("posts")
	public String posts() {
		return"myPage/myPage-posts";
	}
}
