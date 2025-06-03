package kr.co.semi.studyboard.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.semi.member.model.dto.Member;
import kr.co.semi.member.model.service.MemberService;
import kr.co.semi.studyboard.model.dto.Study;
import kr.co.semi.studyboard.model.dto.StudyBoard;
import kr.co.semi.studyboard.model.service.StudyBoardService;
import lombok.extern.slf4j.Slf4j;

@Controller
@SessionAttributes("loginMember")
@RequestMapping("studyBoard")
@Slf4j
public class StudyBoardController {

    @Autowired
    private StudyBoardService service;
    
    @Autowired
    private MemberService mService;
    
    
    
    // ============================================
    // 스터디 스케줄 관련
    // ============================================
    
    /**
     * 스터디 스케줄 페이지
     */
    @GetMapping("calendar")
    public String studyBoardCalendar() {
        return "studyBoard/calendar";
    }
    
  

    // ============================================
    // 스터디 게시판 관련
    // ============================================
    
    /**
     * 스터디 게시판 페이지 (내 게시글, 내 댓글)
     */
    @GetMapping("studyBoard")
    public String studyBoard(@SessionAttribute("loginMember") Member loginMember,
                           @RequestParam(value = "cp",required = false, defaultValue = "1" ) int cp,
                           Model model,
                           @RequestParam Map<String, Object> paramMap) {
        
            Map<String, Object> map = null;
            
            if(paramMap.get("key") == null) {
            	map = service.getStudyInfo(loginMember, cp);
            }else {
            	map = service.searchList(paramMap,cp);
            }
            
            
            model.addAttribute("study", map.get("studyBoardList") );
            model.addAttribute("pagination", map.get("pagination"));
            
        
        return "studyBoard/studyBoard";
    }
    
    // ============================================
    // 스터디 정보 수정 관련
    // ============================================
    
    /**
     * 스터디 정보 수정 페이지
     */
    @GetMapping("update")
    public String studyBoardUpdate(@SessionAttribute("loginMember") Member loginMember,
                                 Model model) {
        try {
            Study study = service.getStudyInfo(loginMember);
            List<Member> member = mService.selectMemberName(loginMember.getStudyNo());
            if (study == null) {
                log.warn("존재하지 않는 스터디 또는 권한 없음 - studyNo: {}, memberNo: {}", 
                		loginMember.getStudyNo(), loginMember.getMemberNo());
                return "redirect:/study/studyNow";
            }
            
            model.addAttribute("study", study);
            model.addAttribute("member",member);
            
            log.info("스터디 정보 수정 페이지 접근 - studyNo: {}, memberNo: {}, isLeader: {}", 
            		loginMember.getStudyNo(), loginMember.getMemberNo(), study.isLeader());
            
        } catch (Exception e) {
            log.error("스터디 정보 수정 페이지 오류", e);
            e.printStackTrace();
            model.addAttribute("errorMessage", "스터디 정보를 불러오는 중 오류가 발생했습니다.");
        }
        
        return "studyBoard/update";
    }

    /**
     * 스터디 정보 수정 처리 - DTO로 받기 (완전 수정 버전)
     */
    @PostMapping("update")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateStudy(@SessionAttribute("loginMember") Member loginMember,
                                                          Study study,  // DTO로 받기
                                                          @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        
        Map<String, Object> response = new HashMap<>();
        try {
    
            
            // 현재 멤버 수보다 최대 인원이 적으면 안됨
            int currentMemberCount = service.getCurrentMemberCount(study.getStudyNo());
            if (study.getStudyMaxCount() < currentMemberCount) {
                response.put("success", false);
                response.put("message", String.format("현재 참여 인원(%d명)보다 적게 설정할 수 없습니다.", currentMemberCount));
                return ResponseEntity.ok(response);
            }
            
            
            // DTO로 받은 데이터를 바로 서비스로 전달
            int result = service.updateStudyInfo(study, imageFile);
            
            response.put("success", result);
            response.put("message", result == 1 ? "스터디 정보가 수정되었습니다." : "수정에 실패했습니다.");
            
            if (result>0) {
                log.info("스터디 정보 수정 성공 - studyNo: {}, memberNo: {}, studyName: {}", 
                        study.getStudyNo(), loginMember.getMemberNo(), study.getStudyName());
            }else {
            	log.info("수정실패");
            }
            
        } catch (Exception e) {
            log.error("스터디 정보 수정 중 오류 발생 - studyNo: {}", study.getStudyNo(), e);
            response.put("success", false);
            response.put("message", "오류가 발생했습니다: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }

    // ============================================
    // 스터디 탈퇴 관련
    // ============================================
    
    /**
     * 스터디 탈퇴 페이지
     */
    @GetMapping("delete")
    public String studyBoardDelete(@SessionAttribute("loginMember") Member loginMember,
                                  Model model) {
        try {
            Study study = service.getStudyInfo(loginMember);
            study.setCurrentMemberCount(service.getCurrentMemberCount(loginMember.getStudyNo())); 
            if(service.isStudyLeader(loginMember.getMemberNo())) {
            	model.addAttribute("isLeader", true);
            } else {
            	
            	model.addAttribute("isLeader", false);
            }
            
            model.addAttribute("study", study);
            model.addAttribute("loginMember", loginMember); // 현재 사용자 정보 추가
            model.addAttribute("members",service.getStudyMembers(loginMember.getStudyNo()));
            
            
            
            
        } catch (Exception e) {
            
            model.addAttribute("errorMessage", "스터디 정보를 불러오는 중 오류가 발생했습니다.");
        }
        
        return "studyBoard/delete";
    }

    /**
     * 스터디 탈퇴 처리 - DTO로 받기 (일반회원)
     */
    @PostMapping("delete")
    public String withdrawFromStudy(@SessionAttribute("loginMember") Member loginMember,
                                                               Study study, RedirectAttributes ra) {  // DTO로 받기
        String message = null;
        try {
        	boolean result = service.withdrawMember(loginMember);
            
            
            if (result) {
                message = "스터디 탈퇴 성공";
                loginMember.setStudyNo(0);
            }
            
        } catch (Exception e) {
           
        }
        
        ra.addFlashAttribute("message",message);
        
        return "redirect:/";
    }
    
    
    
     
  
    /**스터디 팀장인경우 위임후 탈퇴처리
     * @param member
     * @param loginMember
     * @param ra
     * @return
     */
    @PostMapping("transfer")
    public String transferLeadershipAndWithdraw(
            Member member,
            @SessionAttribute("loginMember") Member loginMember,
            RedirectAttributes ra) {
        
        try {
            // 현재 사용자가 팀장인지 확인
            if (!service.isStudyLeader(loginMember.getMemberNo())) {
                ra.addFlashAttribute("message", "팀장만 권한을 위임할 수 있습니다.");
                return "redirect:/studyBoard/delete";
            }
            
            member.setStudyNo(loginMember.getStudyNo());
            
            // 새로운 팀장이 해당 스터디 멤버인지 확인
            if (!service.isStudyMember(member)) {
                ra.addFlashAttribute("message", "선택한 멤버가 스터디에 속해있지 않습니다.");
                return "redirect:/studyBoard/delete";
            }

            // 팀장 권한 위임 및 탈퇴처리
            boolean result = service.transferLeadershipAndWithdraw(member, loginMember);

            if (result) {
                // 세션에서 studyNo 제거
                loginMember.setStudyNo(0);
                
                ra.addFlashAttribute("message", "팀장 권한이 위임되고 스터디에서 탈퇴되었습니다.");
                return "redirect:/";  // 직접 리다이렉트
            } else {
                ra.addFlashAttribute("message", "권한 위임 처리 중 오류가 발생했습니다.");
                return "redirect:/studyBoard/delete";
            }

        } catch (Exception e) {
            ra.addFlashAttribute("message", "오류가 발생했습니다: " + e.getMessage());
            return "redirect:/studyBoard/delete";
        }
    }
    
    // ============================================
    // 스터디 내규 관련
    // ============================================
    
    /**
     * 스터디 내규 페이지
     */
    @GetMapping("rulecontent")
    public String studyBoardRuleContent(@SessionAttribute("loginMember") Member loginMember,
                                       Model model) {
        try {
            Study study = service.getStudyInfo(loginMember);
            study.setRuleContent(service.getStudyrule(loginMember));
//            if (study == null) {
//                log.warn("존재하지 않는 스터디 또는 권한 없음 - studyNo: {}, memberNo: {}", 
//                        loginMember.getStudyNo(), loginMember.getMemberNo());
//                return "redirect:/study/studyNow";
//            }
            
            model.addAttribute("study", study);
            model.addAttribute("studyNo",  loginMember.getStudyNo());
            
            log.info("스터디 내규 페이지 접근 - studyNo: {}, memberNo: {}, isLeader: {}", 
            		 loginMember.getStudyNo(), loginMember.getMemberNo(), study.isLeader());
            
        } catch (Exception e) {
            log.error("스터디 내규 페이지 오류", e);
            model.addAttribute("errorMessage", "스터디 정보를 불러오는 중 오류가 발생했습니다.");
        }
        
        return "studyBoard/rulecontent";
    }

    /**
     * 스터디 내규 등록/수정 처리
     */
    @PostMapping("rulecontent")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateRule(
            @SessionAttribute("loginMember") Member loginMember,
            @RequestBody Map<String, Object> requestData) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 요청 데이터에서 값 추출
            Integer studyNo = (Integer) requestData.get("studyNo");
            String ruleContent = (String) requestData.get("ruleContent");
            
            // 기본 유효성 검사
            if (studyNo == null) {
                response.put("success", false);
                response.put("message", "스터디 번호가 없습니다.");
                return ResponseEntity.ok(response);
            }
            
            if (ruleContent == null || ruleContent.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "내규 내용을 입력해주세요.");
                return ResponseEntity.ok(response);
            }
            
            if (ruleContent.length() > 4000) {
                response.put("success", false);
                response.put("message", "내규 내용은 4000자를 초과할 수 없습니다.");
                return ResponseEntity.ok(response);
            }
            
                       
            // Study 객체 생성
            Study study = new Study();
            study.setStudyNo(studyNo);
            study.setRuleContent(ruleContent.trim());
            
            // 내규 업데이트 실행
            boolean result = service.updateRule(study);
            
            response.put("success", result);
            response.put("message", result ? "내규가 저장되었습니다." : "저장에 실패했습니다.");
            
            if (result) {
                log.info("스터디 내규 수정 성공 - studyNo: {}, memberNo: {}", studyNo, loginMember.getMemberNo());
            } else {
                log.warn("스터디 내규 수정 실패 - studyNo: {}, memberNo: {}", studyNo, loginMember.getMemberNo());
            }
            
        } catch (Exception e) {
            log.error("스터디 내규 수정 중 오류 발생", e);
            response.put("success", false);
            response.put("message", "오류가 발생했습니다: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
    
    // ============================================
    // 유틸리티 메서드들
    // ============================================
    
    /**
     * 스터디 멤버 목록 조회 (AJAX)
     */
    @GetMapping("members")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getStudyMembers(@SessionAttribute("loginMember") Member loginMember) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 스터디 멤버인지 확인
            Study study = service.getStudyInfo(loginMember);
            if (study == null) {
                response.put("success", false);
                response.put("message", "스터디 멤버가 아닙니다.");
                return ResponseEntity.ok(response);
            }
            
            List<Member> members = service.getStudyMembers(loginMember.getStudyNo());
            
            response.put("success", true);
            response.put("members", members);
            response.put("totalCount", members.size());
            
        } catch (Exception e) {
            log.error("스터디 멤버 목록 조회 중 오류 발생", e);
            response.put("success", false);
            response.put("message", "오류가 발생했습니다: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("dissolve")
    public String dissolveStudy(@SessionAttribute("loginMember") Member loginMember,
            RedirectAttributes ra) {
    	
    	try {
    		int result = service.studyDelete(loginMember);
    		if(result>0) {
    			ra.addAttribute("message", "스터디가 해체되었습니다");
    			loginMember.setStudyNo(0);
    		}else {
    			
    			ra.addAttribute("message", "스터디가 해체 실패!");
    		}
			
		} catch (Exception e) {
			ra.addAttribute("message", "스터디가 해체중 오류발생!"+e.getMessage());
		}
    	return"redirect:/";
}
    

	@GetMapping("{studyBoardNo:[0-9]+}")
	public String studyBoardDetail(
							  @PathVariable("studyBoardNo") int studyBoardNo,
							  Model model, 
							  @SessionAttribute(value="loginMember", required=false) Member loginMember,
							  RedirectAttributes ra,
							  HttpServletRequest req, // 요청에 담긴 쿠키 얻어오기
							  HttpServletResponse resp ) { // 새로운 쿠키 만들어서 응답하기
		
		// 게시글 상세 조회 서비스 호출
		
		// 1) Map으로 전달할 파라미터 묶기
		Map<String, Integer> map = new HashMap<>();
		map.put("studyBoardNo", studyBoardNo);
		
		// 로그인 상태인 경우에만 memberNo 추가
		if(loginMember != null) {
			map.put("memberNo", loginMember.getMemberNo());
		}
		
		// 2) 서비스 호출
		StudyBoard studyBoard = service.studyBoardOne(map);
		
		String path = null;
		
		System.out.println(studyBoard);
				
		// 조회 결과가 없는 경우
		if(studyBoard == null) {
			path = "redirect:/studyBoard/"+ "studyBoard"; // 목록 재요청
			ra.addFlashAttribute("message", "게시글이 존재하지 않습니다");
		}else {
			
			//-------- 쿠키를 이용한 조회 수 증가 시작 ---------
			
			// 비회원 또는 로그인한 회원의 글이 아닌 경우 ( == 작성자를 제외한 나머지)
			if(loginMember == null || loginMember.getMemberNo() != studyBoard.getStudyBoardNo()) {
				
				// 요청에 담겨 있는 모든 쿠키 얻어오기
				Cookie[] cookies = req.getCookies();			
				
				Cookie c = null;
				
				for(Cookie temp : cookies) {
					
					// 요청에 담긴 쿠키에 "readBoardNo"가 존재할 때
					if(temp.getName().equals("readStudyBoardNo")) {
						c = temp;
						break;
					}
					
				}
				
				int result = 0; // 조회수 증가 결과를 저장할 변수
				
				// "readStudyBoardNo"가 쿠키에 없을 때
				if(c==null) {
					
					// 새 쿠키 생성("readStudyBoardNo", [게시글 번호])
					c = new Cookie("readStudyBoardNo", "["+studyBoardNo+"]");
					result = service.updateStudyBoardCount(studyBoardNo);
					
				}else {
				// "readBoardNo"가 쿠키에 있을 때
				
					// 현재 게시글을 처음 읽는 경우
					if(c.getValue().indexOf("["+studyBoardNo+"]") == -1) {
						
						// 해당 글 번호를 쿠키에 누적 + 서비스 호출
						c.setValue(c.getValue() + "["+studyBoardNo+"]"); 
						// 그냥 "["+studyBoardNo+"]"만 사용하면
						//기존에 c에 들어가 있던 value들 다 사라지므로 누적을 해야한다
						result = service.updateStudyBoardCount(studyBoardNo);
						
					}
					
				}
				
				// 조회 수 증가 성공 / 조회 성공 시
				if(result > 0) {
					
					// 기존에 조회된 board의 readCount 값을 result 값으로 다시 세팅
					studyBoard.setReadCount(result);
					
					// 쿠키 적용 경로 설정
					c.setPath("/");	// "/" 이하 경로 요청 시 쿠키 서버로 전달
					
					// 쿠키 수명 지정
					// 현재 시간을 얻어오기
					LocalDateTime now = LocalDateTime.now();
					
					// 다음날 자정까지 지정
					LocalDateTime nextDayMidnight = now.plusDays(1)
													.withHour(0)
													.withMinute(0)
													.withSecond(0)
													.withNano(0);
					
					// 현재 시간부터 다음날 자정까지 남은 시간 (초단위)
					long seconds = Duration.between(now, nextDayMidnight).getSeconds();
				 
					// 쿠키 수명 설정
					c.setMaxAge((int)seconds);
					
					resp.addCookie(c); // 응답 객체를 이용해서 클라이언트에게 쿠키 전달
				}
				
				
				
			}
			
			//-------- 쿠키를 이용한 조회 수 증가 종료 ---------
			
			path = "studyBoard/studyBoardDetail"; // board.html로 forward
			
			// board - 게시글 일반 내용 + imageList + commentList
			model.addAttribute("studyBoard", studyBoard);
			System.out.println(studyBoard);
		}
		
		return path;
	}
    
	// 스터디 좋아요
	@PostMapping("like")
	@ResponseBody
	public int studyBoardLike(@RequestBody Map<String, Integer> map) {
		
		return service.studyBoardLike(map);
	}
	
	/**
     * 스터디 채팅 페이지
     */
    @GetMapping("studyChat")
    public String studyChat() {
        return "studyBoard/studyChat";
    }
}