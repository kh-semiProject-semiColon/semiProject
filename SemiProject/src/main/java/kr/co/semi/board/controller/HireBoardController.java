package kr.co.semi.board.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.semi.board.model.dto.HireInfo;
import kr.co.semi.board.model.service.HireBoardService;
import kr.co.semi.member.model.dto.Member;

@Controller
@RequestMapping("hireB")
public class HireBoardController {

	@Autowired
	private HireBoardService service;
	
	@GetMapping("{boardNo:[0-9]+}")
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
		map.put("hireNO", hireNo);
		
		// 로그인 상태인 경우에만 memeberNo 추가
		if(loginMember != null) {
			map.put("memberNo", loginMember.getMemberNo());
		}
		
		// 2) 서비스 호출
		HireInfo hireInfo = service.selectOne(map);
		
		String path = null;
		
		// 조회 결과가 없는 경우
		if(hireInfo == null) {
			path = "redirect:/hire/" + hireNo; // 목록 재요청
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
					result = service.updateReadCount(hireNo);
					
				} else {
					// "readHireNo"가 쿠키에 있을 때
					// "readHireNo" : [2][30][400]
					
					// 현재 개시글을 처음 읽는 경우
					if(c.getValue().indexOf( "["+ hireNo + "]") == -1) {
						
						// 해당 글 번호를 쿠키에 누적 + 서비스 호출
						c.setValue(c.getValue() + "["+ hireNo + "]");
						result = service.updateReadCount(hireNo);
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
			path = "hire/hireDetail";
			
			model.addAttribute("hireInfo", hireInfo);
			
			}
		
		
		return path;
	}
}
