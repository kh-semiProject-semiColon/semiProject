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

	@RequestMapping("info")
	public String info(@SessionAttribute("loginMember") Member loginMember,
								Model model) {
		
		String profileImg = loginMember.getProfileImg();
		String memberNickname = loginMember.getMemberNickname();
		String memberTel = loginMember.getMemberTel();
		String memberEmail = loginMember.getMemberEmail();
		String memberAddress = loginMember.getMemberAddress();
		String memberIntroduce = loginMember.getMemberIntroduce();
		
		if(memberAddress != null) {
			String[] arr = memberAddress.split("\\^\\^\\^");
			
			model.addAttribute("postcode", arr[0]);
			model.addAttribute("address", arr[1]);
			model.addAttribute("detailAddress", arr[2]);
		}
		return "myPage/myPage-info";
	}
	
}
