package kr.co.semi.myPage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.semi.member.model.dto.Member;
import kr.co.semi.myPage.model.service.MyPageService;
import lombok.extern.slf4j.Slf4j;

// 로그인 해야 들어올 수 있으니까 sessionAttributes를 가져왔습니다.
@SessionAttributes({ "loginMember" })
@Controller
@RequestMapping("myPage")
@Slf4j
public class MyPageController {

	@Autowired
	private MyPageService service;

	// 회원가입 시 적었던 내용들을 조회할 수 있게 하는 메서드
	@RequestMapping("info")
	public String info(@SessionAttribute("loginMember") Member loginMember, Model model, Member member) {

		String memberAddress = loginMember.getMemberAddress();

		// 자기소개, 전공 여부는 현재 loginsession에서 가지고 있지 않아 가져와야 한다.
		// 자기소개, 전공 여부, 프로필 사진 들고오는 메서드
		Member selectMember = service.selectMember(loginMember.getMemberNo());

		model.addAttribute("memberMajor", selectMember.getMemberMajor());
		model.addAttribute("memberIntroduce", selectMember.getMemberIntroduce());
		model.addAttribute("profileImg", selectMember.getProfileImg());

		// 주소를 3개 단위로 쪼개 우편번호, 주소, 상세주소로 나누고
		// session에 기입해서 보내는 메서드
		if (memberAddress != null) {
			String[] arr = memberAddress.split("\\^\\^\\^");

			// 초기화
			String postcode = null;
			String address = null;
			String detailAddress = null;

			if (arr.length > 0)
				postcode = arr[0];
			if (arr.length > 1)
				address = arr[1];
			if (arr.length > 2)
				detailAddress = arr[2];

			model.addAttribute("postcode", postcode);
			model.addAttribute("address", address);
			model.addAttribute("detailAddress", detailAddress);

		}

		return "myPage/myPage-info";
	}

	// 게시글 상세주소로 옮기는 메서드 (단순 forward)
	@GetMapping("posts")
	public String posts() {
		return "myPage/myPage-posts";
	}

	// 비밀번호 변경으로 옮기는 메서드 (단순 forward)
	@GetMapping("changePw")
	public String changePw() {

		return "myPage/myPage-changePw";
	}

	// 삭제하기 전 비밀번호 확인 보안 (단순 forward)
	@GetMapping("delete1")
	public String delete1() {

		return "myPage/myPage-delete1";
	}

	// 삭제 경고창이 뜨는 페이지 (단순 forward)
	@GetMapping("delete2")
	public String delete2() {

		return "myPage/myPage-delete2";
	}

	// 삭제하는 페이지로 옮기는 메서드 (단순 forward)
	@GetMapping("delete3")
	public String delete3() {

		return "myPage/myPage-delete3";
	}

	@PostMapping("info")
	public String updateInfo( Member inputMember,
	        @SessionAttribute("loginMember") Member loginMember,
	        RedirectAttributes ra) {

		// 수정되었다는 알림 메세지 띄우는 변수
		String message = null;
		// 제출된 수정된 회원 닉네임, 전화번호, 주소에 로그인한 세션넘버를 같이 보내는 과정
		inputMember.setMemberNo(loginMember.getMemberNo());
		inputMember.setMemberName(loginMember.getMemberName());
		log.info(inputMember.toString());
		
		// 회원정보 수정 메서드
		int result = service.updateInfo(inputMember);

		if (result > 0) {
			loginMember.setMemberNickname(inputMember.getMemberNickname());
			loginMember.setMemberTel(loginMember.getMemberTel());
			loginMember.setMemberAddress(inputMember.getMemberAddress());

			message = "회원 정보 수정 성공!";
		} else {

			message = "회원 정보 수정 실패..";
		}

		ra.addFlashAttribute("message", message);
		return "redirect:info";
	}

}
