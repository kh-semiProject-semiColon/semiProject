package kr.co.semi.myPage.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
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
	@GetMapping("info")
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

	/**
	 * 회원정보 수정 메서드
	 * 
	 * @param inputMember
	 * @param loginMember
	 * @param profileImg
	 * @param ra
	 * @return
	 * @throws Exception
	 */
	@PostMapping("info")
	public String updateInfo(Member inputMember, @SessionAttribute("loginMember") Member loginMember,
			@RequestParam("uploadFile") MultipartFile profileImg, @RequestParam("defaultFile") String defaultFile,
			HttpSession session,
			RedirectAttributes ra) throws Exception {

		// 로그인한 회원의 번호 얻어오기
		int memberNo = loginMember.getMemberNo();

		// 수정되었다는 알림 메세지 띄우는 변수
		String message = null;

		// 제출된 수정된 회원 닉네임, 전화번호, 주소에 로그인한 세션넘버를 같이 보내는 과정
		inputMember.setMemberNo(loginMember.getMemberNo());
		inputMember.setMemberName(loginMember.getMemberName());

		// 사진 삭제 대신 기본 이미지로 수정하는 메서드
		if ("true".equals(defaultFile)) {
			inputMember.setProfileImg("/images/default-profile.png");
			// 빈 파일로 전달 (혹시 서비스에서 null 검사하는 로직 있을 경우 대비)
			profileImg = null;
		} 

		// 회원정보 수정 메서드
		int result = service.updateInfo(inputMember, loginMember, profileImg);

		if (result > 0) {
			loginMember.setMemberNickname(inputMember.getMemberNickname());
			loginMember.setMemberTel(inputMember.getMemberTel());
			loginMember.setMemberAddress(inputMember.getMemberAddress());

			session.setAttribute("loginMember", loginMember); 
			message = "회원 정보 수정 성공!";

		} else {

			message = "회원 정보 수정 실패..";
		}

		ra.addFlashAttribute("message", message);
		return "redirect:info";
	}

	// 비밀번호 변경으로 옮기는 메서드 (단순 forward)
	@GetMapping("changePw")
	public String changePw() {

		return "myPage/myPage-changePw";
	}

	// 비밀번호 변경하는 post 메소드 (위에 단순 forward는 get으로 처리했습니다.)
	@PostMapping("changePw")
	public String changePw(@RequestParam Map<String, String> paramMap,
			@SessionAttribute("loginMember") Member loginMember, RedirectAttributes ra) {

		// 로그인한 회원 번호
		int memberNo = loginMember.getMemberNo();

		// 현재, 새로운 비밀번호, 비밀번호 확인, 회원번호를 서비스로 전달
		int result = service.changePw(paramMap, memberNo);

		String path = null;
		String message = null;

		if (result > 0) { // 변경 성공 시
			message = "비밀번호가 변경되었습니다.";
			path = "/";

		} else {
			// 변경 실패 시
			message = "비밀번호 변경에 실패했습니다.";
			path = "/myPage/changePw";
		}

		ra.addFlashAttribute("message", message);

		return "redirect:" + path;
	}

	@GetMapping("posts")
	public String posts(@SessionAttribute("loginMember") Member loginMember, Model model,
			@RequestParam(value = "cp", required = false, defaultValue = "1") int cp,
			@RequestParam(value = "commentCp", required = false, defaultValue = "1") int commentCp,
			@RequestParam Map<String, Object> paramMap) {

		int memberNo = loginMember.getMemberNo();

		// 페이지네이션 적용된 게시글 조회
		Map<String, Object> boardMap = service.selectBoardWithPaging(memberNo, cp);

		// 게시글 목록과 페이지네이션 정보를 모델에 추가
		model.addAttribute("boardList", boardMap.get("boardList"));
		model.addAttribute("pagination", boardMap.get("pagination"));

		// 댓글 페이지네이션
		Map<String, Object> commentMap = service.selectCommentWithPaging(memberNo, commentCp);

		model.addAttribute("commentList", commentMap.get("commentList"));
		model.addAttribute("commentPagination", commentMap.get("pagination"));

		return "myPage/myPage-posts";
	}

	@GetMapping("delete1")
	public String delete1() {

		return "/myPage/myPage-delete1";
	}

	@PostMapping("delete2")
	public String delete2(@SessionAttribute("loginMember") Member loginMember, @RequestParam("memberPw") String inputPw,
			RedirectAttributes ra) {

		int memberNo = loginMember.getMemberNo();

		boolean isPwMatch = service.checkPassword(memberNo, inputPw);

		if (!isPwMatch) {

			ra.addFlashAttribute("message", "비밀번호가 일치하지 않습니다.");
			return "redirect:/myPage/delete1"; // message alert를 사용하려면 redirect를 사용해야 한다!
		}

		return "myPage/myPage-delete2";
	}

	// 삭제하는 페이지로 옮기는 메서드 (단순 forward)
	@GetMapping("delete3")
	public String delete3() {

		return "myPage/myPage-delete3";
	}

	// 삭제 진행 중 메세지 출력 -> 메인페이지로 이동
	@PostMapping("/delete3")
	public String delete3(@SessionAttribute("loginMember") Member loginMember, SessionStatus status,
			RedirectAttributes ra) {

		int result = service.deleteMember(loginMember.getMemberNo());

		String message = null;
		String path = null;

		if (result > 0) {
			message = "탈퇴 되었습니다.";
			path = "/";

			status.setComplete(); // 세션 완료 시킴 (로그아웃)

		} else {
			message = "서버 에러가 발생했습니다. 다시 시도해주세요.";
			path = "/myPage/delete1";

		}

		ra.addFlashAttribute("message", message);
		return "redirect:" + path;
	}

}