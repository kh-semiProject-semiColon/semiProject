package kr.co.semi.member.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.semi.board.model.service.StudyCalendarService;
import kr.co.semi.member.model.dto.Member;
import kr.co.semi.member.model.service.MemberService;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("member")
@Slf4j
@SessionAttributes({"loginMember"})
public class MemberController {
	
	@Autowired
	private MemberService service;
	
	@Autowired
	private StudyCalendarService studyCalendarService;
	
	/*
	 * [로그인]
	 * - 특정 사이트에 아이디/비밀번호 등을 입력해서
	 * 	 해당 정보가 DB에 있으면 조회/서비스 이용
	 * 
	 * - 로그인 한 회원 정보는 session에 기록하여
	 * 로그아웃 또는 브라우저 종료 시 까지
	 * 해당 정보를 계속 이용할 수 있게 함
	 * 
	 */
	
	/** 로그인 by 김성원
	 * @param inputMember : 커맨드 객체 (@ModelAttribute 생략)
	 * 						memberEmail, memberPw 세팅된 상태
	 * @param ra : 리다이렉트 시 request scope -> session scope -> request 로 데이터 전달
	 * @param model : 데이터 전달용 객체(기본 request scope)
	 * 				/ (@SessionAttributes 어노테이션과 함께 사용 시 session scope 이동)
	 * @return 
	 */
	@PostMapping("login")
	public String login(Member inputMember, RedirectAttributes ra, Model model,
				@RequestParam(value="saveId", required = false) String saveId,
				HttpServletResponse resp) {
		
		// 로그인 서비스 호출
		Member loginMember = service.login(inputMember);
		
		
		// 로그인 실패 시
		if(loginMember == null) {
			ra.addFlashAttribute("message", "이메일 또는 비밀번호가 일치하지 않습니다.");
		} else {
			// 로그인 성공 시
			if(studyCalendarService.bringStudyNo(loginMember.getMemberNo()) == 0) {
				// 스터디가 없는 회원은 스터디 넘버를 0으로 설정
				loginMember.setStudyNo(0);
			}else {
				
				loginMember.setStudyNo(studyCalendarService.bringStudyNo(loginMember.getMemberNo()));
			}
			

			// session scope에 loginMember 추가
			model.addAttribute("loginMember", loginMember);
			// 1단계 : model을 이용하여 request scope에 세팅됨
			// 2단계 : 클래스 위에 @SessionAttributes() 어노테이션 작성하여
			//			session scope로 loginMember를 이동
			
			// ************* Cookie *****************
			// 로그인 시 작성한 이메일 저장 (쿠키에)
			
			// 쿠키 객체 생성(k:v)
			Cookie cookie = new Cookie("saveId", loginMember.getMemberEmail());
			// saveId=user01@kh.or.kr
			
			// 쿠키가 적용될 경로 설정
			// -> 클라이언트가 어떤 요청을 할 때 쿠키를 첨부할지 지정
			// ex) "/" : IP 또는 도메인 또는 localhost
			//			--> 메인페이지 + 그 하위 주소 모두
			cookie.setPath("/");
			
			// 쿠키의 만료 기간 지정
			if(saveId != null) { // 아이디 저장 체크박스 체크했을 때
				cookie.setMaxAge(60 * 60 * 24 * 30); // 30일 (초단위로 지정)
				
			} else { // 체크 안했을 때
				cookie.setMaxAge(0); // 0초 (클라이언트 쿠키 삭제)
				
			}
			
			// 응답 객체에 쿠키 추가 -> 클라이언트에게 전달
			resp.addCookie(cookie);
			
		}
		
		
		
		return "redirect:/"; // 메인페이지로 재요청
	}
	
	/** 로그아웃 : session에 저장된 로그인된 회원 정보를 없앰
	 * @param SessionStatus : @SessionAttributes로 지정된 특정 속성을 세션에서 제거 기능 제공 객체
	 * @return
	 * by 김성원
	 */
	@GetMapping("logout")
	public String logout(SessionStatus status) {
		
		status.setComplete(); // 세션을 완료 시킴 ( == @SessionAttributes로 등록된 세션 제거)
		
		return "redirect:/";
	}

	
	/** 로그인페이지 STEP1로 이동
	 * @return
	 */
	@RequestMapping("signup")
	public String signup() {
		
		return "member/signup";
	}
	
	/** 로그인페이지 STEP2로 이동
	 * @return
	 */
	@RequestMapping("signup2")
	public String signup2() {
		
		return "member/signup2";
	}
	
	/** 로그인페이지 STEP3로 이동
	 * @return
	 */
	@RequestMapping("signup3")
	public String signup3() {
		
		return "member/signup3";
	}
	
	/** ID 찾기 페이지로 이동
	 * @return
	 */
	@RequestMapping("findId")
	public String fingId() {
		
		return "member/findId";
	}
	
	/** 회원가입 STEP2에서 입력한 기본 정보를 DB에 저장
	 * @param inputMember : input창에 입력한 정보들을 DTO에 담아 가져오기
	 * @param memberAddress : 해당하는 memberAddress가 3개로 나뉘어있기 때문에 배열로 묶어서 가져옴 
	 * @return
	 */
	@PostMapping("signupInfo")
	public String signupInfo(Member inputMember,
							@RequestParam("memberAddress") String[] memberAddress
							) {
		
		// 회원가입 서비스 호출
		int result = service.signupInfo(inputMember, memberAddress);
		
		String path = null;
		
		if(result > 0) { // 회원가입 성공시
			path = "signup3";
		}else {// 회원가입 실패
			path = "signup2";
		}
		
		return "redirect:"+path;
	}
	
	/** 이메일(아이디) 중복 검사
	 * @param memberEmail : fetch로 보낸 쿼리로 받음
	 * @return
	 */
	@ResponseBody 
	@GetMapping("checkEmail")
	public int checkEmail(@RequestParam("memberEmail") String memberEmail) {
		
		return service.checkEmail(memberEmail);
	}
	
	/** 닉네임 중복 검사
	 * @param memberNickname : fetch로 보낸 쿼리로 받음
	 * @return 중복 1, 아님 0
	 */
	@ResponseBody
	@GetMapping("checkNickname")
	public int checkNickname(@RequestParam("memberNickname") String memberNickname) {
		
		return service.checkNickname(memberNickname); 
	}
	
	/** ID 찾기
	 * @param inputMember : input에 작성한 이름과 전화번호를 obj로 담아 fetch로 보낸 것을 받았다
	 * @return
	 */
	@ResponseBody
	@PostMapping("checkName")
	public int checkName(@RequestBody Member inputMember) {
		
		System.out.println(inputMember.getMemberName());
		System.out.println(inputMember.getMemberTel());
		
		int result = service.checkName(inputMember);
		
		return result;
	}
	
	/** ID 찾기 결과창으로 이동
	 * @param inputMember : ID찾기 input에 작성한 이름과 전화번호를 다음으로 버튼 클릭시 form으로 제출
	 * @param model : 작성한 이름과 그를 바탕으로 얻어낸 일치하는 회원의 아이디(이메일)을 저장하여 다음 페이지에 출력한다.
	 * @return
	 */
	@PostMapping("findIdResult")
	public String findIdResult(Member inputMember, Model model) {
		
		String memberEmail = service.getId(inputMember);
		
		model.addAttribute("memberName", inputMember.getMemberName());
		model.addAttribute("memberEmail",memberEmail);
		
		return "/member/findIdResult";
	}
	
	/** 비밀번호 찾기 페이지 이동
	 * @return
	 */
	@GetMapping("findPw")
	public String findPw() {
		return "/member/findPw";
	}

	/** 인증 번호 받기 전 DB에 입력한 이름, 이메일 동일한 회원 있는지 조회
	 * @param inputMember
	 * @return
	 */
	@ResponseBody
	@PostMapping("checkNM")
	public int checkNM(@RequestBody Map<String, String> map) {
		
	
		
		int result = service.checkNM(map);
		
		return result;
	}
	
	
	/** 비밀번호 찾기 첫페이지(findPw)에서 다음 버튼을 누르고 수정페이지로 넘어감
	 * @param inputMember input에 입력한 회원 정보를 가져옴
	 * @param model 가져온 회원 정보 중 아이디를 model에 저장하여 넘김
	 * @return
	 */
	@PostMapping("modifyPw")
	public String modifyPw(Member inputMember, Model model) {
		
		model.addAttribute("memberEmail", inputMember.getMemberEmail());
		
		return "/member/modifyPw";
	}
	
	/** 비밀번호 수정
	 * @param inputMember 입력한 비밀번호와 input/hidden에 저장한 memberEmail값
	 * @return
	 */
	@PostMapping("changePw")
	public String changePw(Member inputMember) {
		
		System.out.println(inputMember.getMemberEmail());
		System.out.println(inputMember.getMemberPw());
		
		String path = "";
		String message = "";
		
		int result = service.changePw(inputMember);
		
		if(result == 1) {
			path = "/member/pwChanged";
		}else {
			path = "/";
			message = "비밀번호 변경에 실패했습니다";
		}
		
		return "redirect:"+path;
	}
	
	/** 비밀번호 수정 완료 페이지로 이동
	 * @return
	 */
	@GetMapping("pwChanged")
	public String pwChanged() {
		return "/member/pwChanged";
	}
}
