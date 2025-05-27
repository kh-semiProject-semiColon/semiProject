package kr.co.semi.board.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.semi.board.model.dto.Board;
import kr.co.semi.board.model.service.BoardService;
import kr.co.semi.board.model.service.HireBoardService;
import kr.co.semi.member.model.dto.Member;
import kr.co.semi.studyboard.model.dto.Study;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("board")
@Slf4j
public class BoardController {
	
	@Autowired
	private HireBoardService service;
	@Autowired
	private BoardService bService;

	/** 구인 게시판 조회 
	 * @return
	 */
	@GetMapping("hire")
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
	
	@GetMapping("detail")
	public String showBoardDetail() {
		
		return "hire/boardDetail";
	}
	
	@GetMapping("write")
	public String writeHireBoard(@SessionAttribute("loginMember") Member loginMember,
			  					  Model model,
			  					 RedirectAttributes ra) {
		String path;
		
		int memberNo = loginMember.getMemberNo();
		
	    List<Study> studyList = service.showStudySelect(memberNo);
	    
	    
	    if(studyList.isEmpty() || studyList == null) {
	    	path = "redirect:/board/hire"; // 목록 재요청
	    	ra.addFlashAttribute("message", "생성된 스터디가 없습니다.");
	    	
	    } else {
	    	
	    	model.addAttribute("study", studyList);
	    	path = "hire/hireWrite";
	    	
	    }
	    
	    
		return path;
	}
	
//	@PostMapping("write")
//	public String writehireBoardInsert(@ModelAttribute HireInfo inputHire,
//									   @SessionAttribute("loginMember") Member loginMember,
//									   RedirectAttributes ra
//									   ) {
//		
//		
//		
//		return "redirect:/board/hire";
//	}
//	
	
	@GetMapping("announce")
	public String announce(Model model,
    		@RequestParam(value="cp", required=false, defaultValue ="1") int cp, 
    		@RequestParam Map<String, Object> paramMap) {
		
    	Map<String, Object> map = null;
    	
    	if(paramMap.get("key") == null) {
    		
    		map = bService.selectAllAnnounce(cp);
    	}else {
    		map = bService.searchAnnounceList(paramMap, cp);
    	}
    	
    	model.addAttribute("announce",map.get("announce"));
    	model.addAttribute("pagination", map.get("pagination"));
		
		return "board/announce";
	}
	
	@GetMapping("{boardCode:[0-9]+}")
	public String boardList(Model model,
			@PathVariable("boardCode") int boardCode,
			@RequestParam(value="cp", required=false, defaultValue ="1") int cp, 
			@RequestParam Map<String, Object> paramMap) {
		
		Map<String, Object> map = null;
		
		if(paramMap.get("key") == null) {
			
			map = bService.selectBoardList(boardCode,cp);
		}else {
			paramMap.put("boardCode", boardCode);
			map = bService.searchList(paramMap, cp);
		}
		
		model.addAttribute("boardList",map.get("boardList"));
		model.addAttribute("pagination", map.get("pagination"));
		model.addAttribute("boardCodeNo", boardCode);
		
		return "board/boardList";
	}
	
	
//	/** 게시글 상세 조회
//	 * @param boardCode		: 주소에 포함된 게시판 종류 번호 (1,2)
//	 * @param boardNo		: 주소에 포함된 게시글 번호
//	 * 		 (boardCode, boardNo는 Request scope에 저장되어 있음)
//	 * 		-> PathVariable 어노테이션 사용 시 변수 값이 자동으로 request scope에 저장된다		
//	 * @param model			
//	 * @param loginMember	: 로그인 여부와 관련 없이 상세조회는 가능해야 하기 때문에 required=false로 작성
//	 * @param ra
//	 * @return
//	 */
//	@GetMapping("{boardCode:[0-9]+}/{boardNo:[0-9]+}")
//	public String boardDetail(@PathVariable("boardCode") int boardCode,
//							  @PathVariable("boardNo") int boardNo,
//							  Model model, 
//							  @SessionAttribute(value="loginMember", required=false) Member loginMember,
//							  RedirectAttributes ra,
//							  HttpServletRequest req, // 요청에 담긴 쿠키 얻어오기
//							  HttpServletResponse resp ) { // 새로운 쿠키 만들어서 응답하기
//		
//		// 게시글 상세 조회 서비스 호출
//		
//		// 1) Map으로 전달할 파라미터 묶기
//		Map<String, Integer> map = new HashMap<>();
//		map.put("boardCode", boardCode);
//		map.put("boardNo", boardNo);
//		
//		// 로그인 상태인 경우에만 memberNo 추가
//		if(loginMember != null) {
//			map.put("memberNo", loginMember.getMemberNo());
//		}
//		
//		// 2) 서비스 호출
//		Board board = service.selectOne(map);
//		
//		String path = null;
//				
//		// 조회 결과가 없는 경우
//		if(board == null) {
//			path = "redirect:/board/"+ boardCode; // 목록 재요청
//			ra.addFlashAttribute("message", "게시글이 존재하지 않습니다");
//		}else {
//			
//			//-------- 쿠키를 이용한 조회 수 증가 시작 ---------
//			
//			// 비회원 또는 로그인한 회원의 글이 아닌 경우 ( == 작성자를 제외한 나머지)
//			if(loginMember == null || loginMember.getMemberNo() != board.getBoardNo()) {
//				
//				// 요청에 담겨 있는 모든 쿠키 얻어오기
//				Cookie[] cookies = req.getCookies();			
//				
//				Cookie c = null;
//				
//				for(Cookie temp : cookies) {
//					
//					// 요청에 담긴 쿠키에 "readBoardNo"가 존재할 때
//					if(temp.getName().equals("readBoardNo")) {
//						c = temp;
//						break;
//					}
//					
//				}
//				
//				int result = 0; // 조회수 증가 결과를 저장할 변수
//				
//				// "readBoardNo"가 쿠키에 없을 때
//				if(c==null) {
//					
//					// 새 쿠키 생성("readBoardNo", [게시글 번호])
//					c = new Cookie("readBoardNo", "["+boardNo+"]");
//					result = service.updateReadCount(boardNo);
//					
//				}else {
//				// "readBoardNo"가 쿠키에 있을 때
//				
//					// 현재 게시글을 처음 읽는 경우
//					if(c.getValue().indexOf("["+boardNo+"]") == -1) {
//						
//						// 해당 글 번호를 쿠키에 누적 + 서비스 호출
//						c.setValue(c.getValue() + "["+boardNo+"]"); 
//						// 그냥 "["+boardNo+"]"만 사용하면
//						//기존에 c에 들어가 있던 value들 다 사라지므로 누적을 해야한다
//						result = service.updateReadCount(boardNo);
//						
//					}
//					
//				}
//				
//				// 조회 수 증가 성공 / 조회 성공 시
//				if(result > 0) {
//					
//					// 기존에 조회된 board의 readCount 값을 result 값으로 다시 세팅
//					board.setReadCount(result);
//					
//					// 쿠키 적용 경로 설정
//					c.setPath("/");	// "/" 이하 경로 요청 시 쿠키 서버로 전달
//					
//					// 쿠키 수명 지정
//					// 현재 시간을 얻어오기
//					LocalDateTime now = LocalDateTime.now();
//					
//					// 다음날 자정까지 지정
//					LocalDateTime nextDayMidnight = now.plusDays(1)
//													.withHour(0)
//													.withMinute(0)
//													.withSecond(0)
//													.withNano(0);
//					
//					// 현재 시간부터 다음날 자정까지 남은 시간 (초단위)
//					long seconds = Duration.between(now, nextDayMidnight).getSeconds();
//				 
//					// 쿠키 수명 설정
//					c.setMaxAge((int)seconds);
//					
//					resp.addCookie(c); // 응답 객체를 이용해서 클라이언트에게 쿠키 전달
//				}
//				
//				
//				
//			}
//			
//			
//			
//			
//			
//			//-------- 쿠키를 이용한 조회 수 증가 종료 ---------
//			
//			
//			
//			path = "board/boardDetail"; // board.html로 forward
//			
//			// board - 게시글 일반 내용 + imageList + commentList
//			model.addAttribute("board", board);
//			
//			// 조회된 이미지가 있는 경우
//			if(!board.getImageList().isEmpty()) {
//				
//				BoardImg thumbnail = null;
//				
//				// imageList의 0번 인덱스 == 가장 빠른 순서(imgOrder)
//				// 만약 이미지 목록의 첫번째 행의 imgOrder가 0 == 썸네일인 경우
//				if(board.getImageList().get(0).getImgOrder() == 0) {
//					
//					thumbnail = board.getImageList().get(0);
//				}
//				
//				model.addAttribute("thumbnail", thumbnail);
//				model.addAttribute("start", thumbnail != null ? 1 : 0);
//					// start : 썸네일이 있다면 1, 없다면 0을 저장
//				
//			}
//		}
//		
//		return path;
//	}
//	
//	/** 게시글 좋아요 체크/해제
//	 * @return
//	 */
//	@PostMapping("like")
//	@ResponseBody
//	public int boardLike(@RequestBody Map<String, Integer> map) {
//		
//		return service.boardLike(map);
//	}
//	
}
