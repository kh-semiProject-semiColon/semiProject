package kr.co.semi.board.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.semi.board.model.dto.HireComment;
import kr.co.semi.board.model.dto.HireInfo;
import kr.co.semi.board.model.service.HireBoardService;
import kr.co.semi.member.model.dto.Member;
import kr.co.semi.studyboard.model.dto.Study;

@Controller
@RequestMapping("hire")
public class HireBoardController {

    private final BoardController boardController;

	@Autowired
	private HireBoardService service;

    HireBoardController(BoardController boardController) {
        this.boardController = boardController;
    }
	
	/** 구인 게시판 조회 
	 * @return
	 */
	@GetMapping("board")
	public String showHireBoard(@RequestParam(value = "cp", required = false, defaultValue = "1") int cp,
							    Model model,
							    @RequestParam Map<String, Object> paramMap) {
		
		Map<String, Object> map = null;
		
		if(paramMap.get("key") == null) {
			map = service.showHireBoard(cp);
		} else {

			map = service.searchList(paramMap, cp);
			
		}
		
		model.addAttribute("pagination", map.get("pagination"));
		model.addAttribute("hireList", map.get("hireList"));
		
	
		return "hire/hireBoard";
	}
	
	// 구인 게시글 작성 페이지로 이동
	@GetMapping("write")
	public String writeHireBoard(@SessionAttribute("loginMember") Member loginMember,
			  					  Model model,
			  					 RedirectAttributes ra) {
		String path;
		
		int memberNo = loginMember.getMemberNo();
		
	    List<Study> study = service.showStudySelect(memberNo);

	    
	    List<Study> studyList = new ArrayList<>();
	    
	    for (Study s : study) {
	        if ("Y".equals(s.getStudyCap())) {
	        	int currentMemberCount = service.hireCount(s.getStudyNo());
	            s.setCurrentMemberCount(currentMemberCount);
	        	
	        	studyList.add(s);
	        	
	        	System.out.println(s.getCurrentMemberCount());
	    	    
	    	    
	    	    
	        }
	    }
	    
	    
	    if(study.isEmpty() || study == null) {
	    	path = "redirect:/hire/board"; // 목록 재요청
	    	ra.addFlashAttribute("message", "생성된 스터디가 없습니다.");
	    	
	    } else {
	    	
	    	if(studyList.isEmpty() || studyList == null) {
	    		
	    		path = "redirect:/hire/board"; // 목록 재요청
	    		ra.addFlashAttribute("message", "스터디의 리더만 작성할 수 있습니다");
	    		
	    	} else {
	    		
	    		model.addAttribute("study", studyList);
	    		path = "hire/hireWrite";
	    		
	    	}
	    	
	    }
	    
	    
		return path;
	}
	
	// 구인 게시글 업로드
	@PostMapping("write")
	public String writeHireBoardInsert(@ModelAttribute HireInfo inputHire,
									   @SessionAttribute("loginMember") Member loginMember,
									   RedirectAttributes ra
									   )throws Exception {
		
		int hireNo = 0;
		
//		System.out.println(inputHire);
		
		inputHire.setMemberNo(loginMember.getMemberNo());
		inputHire.setStudyNo(loginMember.getStudyNo());
		
		hireNo = service.writeHireBoardInsert(inputHire);
		
		// 게시판 번호 조회
		
		
		String path = null;
		String message = null;
		
		if(hireNo > 0) {
			message = "게시글이 작성되었습니다!";
			path = "/hire/detail/" + hireNo;
			
		} else {
			
			path = "/hire/board";
			message = "게시글 작성 실패";
		}
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:" + path;
		
	}
	
	
	// 구인 게시글 상세
	@GetMapping("detail/{hireNo:[0-9]+}")
	public String boardDetail(@PathVariable("hireNo") int hireNo,
							  Model model,
							  @SessionAttribute(value="loginMember", required = false) Member loginMember,
							  RedirectAttributes ra,
							  HttpServletRequest req,	// 요청에 담긴 쿠키 얻어오기
							  HttpServletResponse resp	// 새로운 쿠키 만들어서 응답하기
							  ) {
		
		// 게시글 상세 조회 서비스 호출
		
		// 1) Map으로 전달할 파라미터 묶기
		Map<String, Integer> map = new HashMap<>();
		map.put("hireNo", hireNo);
		
		// 로그인 상태인 경우에만 memeberNo 추가
		if(loginMember != null) {
			map.put("memberNo", loginMember.getMemberNo());
		}
		
		// 2) 서비스 호출
		HireInfo hireInfo = service.selectOne(map);
		
		String path = null;
		
		// 조회 결과가 없는 경우
		if(hireInfo == null) {
			path = "redirect:/hire/board"; // 목록 재요청
			ra.addFlashAttribute("message", "게시글이 존재하지 않습니다");
			
		} else {
			/* ---------------------------쿠키를 이용한 조회수 증가 시작--------------------------- */
			
			// 비회원 또는 로그인한 회원의 글이 아닌 경우 ( == 글쓴이를 뺀 다른 사람 )
			if(loginMember == null || loginMember.getMemberNo() != hireInfo.getMemberNo()) {
				
				// 요청에 담겨있는 모든 쿠키 얻어오기
				Cookie[] cookies = req.getCookies();
				
				Cookie c = null;
				
				for(Cookie temp : cookies) {
					
					// 요청에 담긴 쿠키에 "readHireNo"가 존재할 때
					if(temp.getName().equals("readHireNo")) {
						c = temp;
						break;
					}
				}
				
				int result = 0; // 조회수 증가 결과를 저장할 변수
				
				// "readHireNo"가 쿠키에 없을 때
				if(c == null) {
					
					// 새 쿠키 생성 ("readHireNo", [게시글 번호])
					c = new Cookie("readHireNo", "["+ hireNo + "]");
					result = service.updateHireReadCount(hireNo);
					
				} else {
					// "readHireNo"가 쿠키에 있을 때
					// "readHireNo" : [2][30][400]
					
					// 현재 개시글을 처음 읽는 경우
					if(c.getValue().indexOf( "["+ hireNo + "]") == -1) {
						
						// 해당 글 번호를 쿠키에 누적 + 서비스 호출
						c.setValue(c.getValue() + "["+ hireNo + "]");
						result = service.updateHireReadCount(hireNo);
					}
					
				}
				
				// 조회 수 증가 성공 / 조회 성공 시
				if(result > 0) {
					
					// 먼저 조회된 hireInfo의 readCount 값을
					// result 값으로 다시 세팅
					hireInfo.setHireReadCount(result);
					
					// 쿠키 적용 경로 설정
					c.setPath("/");	// "/" 이하 경로 요청 시 쿠키 서버로 전달
					
					// 쿠키 수명 지정
					// 현재 시간을 얻어오기
					LocalDateTime now = LocalDateTime.now();
					
					// 다음날 자정 지정
					LocalDateTime nextDayMidnight = now.plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
					
					// 현재 시간부터 다음날 자정까지 남은 시간 계산 (초단위)
					long seconds = Duration.between(now, nextDayMidnight).getSeconds();
					
					// 쿠키 수명 설정
					c.setMaxAge((int)seconds);
					
					resp.addCookie(c); // 응답 객체를 이용해서 클라이언트에게 쿠키 전달
				}
			}
			
			
			/* ---------------------------쿠키를 이용한 조회수 증가 끝--------------------------- */
			
			// 조회 결과가 있는 경우
			
			// 스터디 정보 조회
			Study study = service.selectStudyNo(hireInfo.getStudyNo());
			
			int currentMemberCount = service.hireCount(hireInfo.getStudyNo());
			
			study.setCurrentMemberCount(currentMemberCount);
			
			model.addAttribute("loginMember", loginMember);
			model.addAttribute("hireInfo", hireInfo);
			model.addAttribute("study", study);
			
			path = "hire/hireDetail";
			
			
			}
		
		
		return path;
	}
	
	// 구인게시글 수정 페이지로 이동
	@GetMapping("edit/{hireNo:[0-9]+}")
	public String boardUpdate(@PathVariable("hireNo") int hireNo,
							  @SessionAttribute("loginMember") Member loginMember,
							  Model model,
							  RedirectAttributes ra) {
		
		// 수정 화면에 출력할 기존의 제목/내용/이미지 조회
		// -> 게시글 상세 조회
		Map<String, Integer> map = new HashMap<>();
		map.put("hireNo", hireNo);
		
		HireInfo hireInfo = service.selectOne(map);
		
		Study study = service.selectStudyNo(hireInfo.getStudyNo());
		
		String message = null;
		String path = null;
		
		if(hireInfo == null) {
			message = "해당 게시글이 존재하지 않습니다";
			path = "redirect:/hire/board"; // 메인페이지로 리다이렉트
			
			ra.addFlashAttribute("message", message);
			
		} else if(hireInfo.getMemberNo() != loginMember.getMemberNo()) {
			message = "자신이 작성한 글만 수정이 가능합니다!";
			
			// 해당 글 상세조회 리다이렉트 (/board/1/2000)
			path = String.format("redirect:/hire/%d", hireNo);
			
			ra.addFlashAttribute("message", message);
			
		} else {
			
			path = "hire/hireUpdate";
			model.addAttribute("hireInfo",hireInfo);
			model.addAttribute("study",study);
			
		}
		
		return path;
	}
	
	// 구인 게시글 수정
	@PostMapping("edit/{hireNo:[0-9]+}/update")
	public String hireBoardUpdate(@PathVariable("hireNo") int hireNo,
			  				  @ModelAttribute HireInfo inputHire,
			  				  @SessionAttribute("loginMember") Member loginMemeber,
			  				  RedirectAttributes ra,
			  				  @RequestParam(value="cp", required = false, defaultValue = "1") int cp
			  				  ) throws Exception {
		
		// 1. 커맨드 객체 (inputBoard)에 boardCode, boardNo, memberNo 세팅
		inputHire.setHireNo(hireNo);
		inputHire.setMemberNo(loginMemeber.getMemberNo());
		// inputBoard -> 제목, 내용, boardCode, boardNo, memberNo
		
//		System.out.println(inputHire);
		
		// 2. 게시글 수정 서비스 호출 후 결과 반환 받기
		int result = service.hireUpdate(inputHire);
		
		// 3. 서비스 결과에 따라 응답 제어
		String message = null;
		String path = null;
		
		if(result > 0) {
			message = "게시글이 수정 되었습니다";
			// 게시글 상세조회 페이지로 redirect
			// /board/1/2000?cp=3
			path = String.format("/hire/detail/%d?cp=%d", hireNo, cp);
			
		} else {
			message = "수정 실패";
			path = String.format("/hire/detail/%d?cp=%d", hireNo, cp);
			// 목표 : editBoard/1/2004/update?cp=1 (GET)
			// 게시글 수정 화면으로 다시 요청!
		}
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:" + path;
	}
	
	// 구인 게시글 삭제
	@RequestMapping(value = "delete/{hireNo:[0-9]+}", 
					method = {RequestMethod.GET, RequestMethod.POST})
	public String hireDelete(@PathVariable("hireNo") int hireNo,
						     @RequestParam(value="cp", required = false, defaultValue = "1") int cp,
						     RedirectAttributes ra,
						     @SessionAttribute("loginMember") Member loginMember) {
	
	Map<String, Integer> map = new HashMap<>();
	map.put("hireNo", hireNo);
	map.put("memberNo", loginMember.getMemberNo());
	
	int result = service.hireDelete(map);
	
	String path = null;
	String message = null;
	
	if(result > 0) {
		message = "삭제 되었습니다";
		
		path = String.format("/hire/board?cp=%d", cp); // 게시글 목록 조회
		// /board/1?cp=7
		
	} else {
		message = "삭제 실패";
		
		path = String.format("/hire/detail/%d?cp=%d", hireNo, cp); //  게시글 상세 조회 
		// /board/1/2000?cp=7
	}
	
	ra.addFlashAttribute("message", message);
	
	return "redirect:" + path;
	}
	
	//멤버초대 - 초대할때 댓글 사용자 memberNo/ 글번호 불러와야함 초대 버튼은 글 주인만 == 로그인 멤버 같을때 볼 수 있음
	@PostMapping("invite/{hireNo:[0-9]+}")
	@ResponseBody
	public String memberInvite(@PathVariable("hireNo") int hireNo,
							   RedirectAttributes ra,
							   @RequestBody Map<String, Integer> requestData) {
		
		int memberNo = requestData.get("memberNo");
		String message = null;
		String path = null;
		
		int studyNo = service.getStudyNo(hireNo);
		
		Map<String, Integer> map = new HashMap<>();
		map.put("studyNo", studyNo);
		map.put("memberNo", memberNo);
		
//		이미 초대한 회원인지 검사
		int invitation = service.invitation(map);
		
		if(invitation > 0) {
			
			message = "은 이미 초대한 회원입니다";
			
		} else {
			
			int result = service.memberInvite(map);
			
			if(result > 0) {
				message = "을 초대했습니다";

				
			} else {
				message = " 초대실패";

			}
		}
		
		return message;
	}
	
	@GetMapping("/popup")
	@ResponseBody
	public Map<String, String> getResume(@RequestParam("memberNo") int memberNo) {
	    Member member = service.getResumeByMemberNo(memberNo);
	    Map<String, String> result = new HashMap<>();
	    result.put("memberNickname", member.getMemberNickname()); 
	    result.put("memberIntroduce", member.getMemberIntroduce()); 
	    result.put("profileImg", member.getProfileImg()); 
	    result.put("memberMajor", member.getMemberMajor()); 
	    return result;
	}
}
