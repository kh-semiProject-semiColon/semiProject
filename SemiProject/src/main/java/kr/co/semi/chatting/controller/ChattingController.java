package kr.co.semi.chatting.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.semi.chatting.model.dto.Chatting;
import kr.co.semi.chatting.model.service.ChattingService;
import kr.co.semi.member.model.dto.Member;
import kr.co.semi.member.model.service.MemberService;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/study/{studyNo}/chat")
@Slf4j
public class ChattingController {

    @Autowired
    private ChattingService service;
    
    @Autowired
    private MemberService memberService;

    @GetMapping("")
    public String showChatPage(@PathVariable("studyNo") int studyNo, 
                              Model model,
                              @SessionAttribute("loginMember") Member loginMember) {
        
        log.info("채팅 페이지 접근 - studyNo: {}, loginMember: {}", studyNo, loginMember.getMemberNo());
        
        try {
            // 사용자가 해당 스터디의 멤버인지 확인 (URL의 studyNo 사용)
            boolean isStudyMember = service.isStudyMember(studyNo, loginMember.getMemberNo());
            if (!isStudyMember) {
                log.warn("비인가 접근 시도: 사용자 {}는 스터디 {}의 멤버가 아님", 
                        loginMember.getMemberNo(), studyNo);
                model.addAttribute("error", "해당 스터디의 멤버만 접근할 수 있습니다.");
                return "error/403";
            }
            
            // 메시지 목록 조회 (URL의 studyNo 사용 - 이게 핵심!)
            List<Chatting> messages = service.getMessagesByStudyNo(studyNo);
            log.info("조회된 메시지 수: {}", messages != null ? messages.size() : 0);
            
            // 메시지 내용 로깅 (디버깅용)
            if (messages != null && !messages.isEmpty()) {
                log.info("첫 번째 메시지: messageNo={}, senderNo={}, content={}", 
                        messages.get(0).getMessageNo(), 
                        messages.get(0).getSenderNo(), 
                        messages.get(0).getContent());
            }
            
            // 멤버 정보 매핑 (URL의 studyNo 사용)
            Map<Integer, String> memberMap = getMemberMapForStudy(studyNo);
            log.info("멤버 맵: {}", memberMap);
            
            // 모델에 데이터 추가
            model.addAttribute("messages", messages);
            model.addAttribute("studyNo", studyNo);  // URL의 studyNo 사용
            model.addAttribute("loginMember", loginMember);
            model.addAttribute("memberMap", memberMap);
            
            log.info("=== 최종 전달 데이터 ===");
            log.info("studyNo: {}", studyNo);
            log.info("messages 개수: {}", messages != null ? messages.size() : 0);
            log.info("memberMap: {}", memberMap);
            log.info("=====================");
            
            return "studyBoard/studyChat";
            
        } catch (Exception e) {
            log.error("채팅 페이지 로드 중 오류 발생", e);
            model.addAttribute("error", "채팅을 불러오는데 실패했습니다.");
            return "error/500";
        }
    }
    
    @GetMapping("/messages")
    @ResponseBody
    public Map<String, Object> getStudyChatMessages(@PathVariable("studyNo") int studyNo,
                                                   @RequestParam(value = "lastMessageNo", defaultValue = "0") int lastMessageNo,
                                                   @SessionAttribute("loginMember") Member loginMember) {
        
        log.info("AJAX 메시지 목록 조회 요청 - studyNo: {}, lastMessageNo: {}", studyNo, lastMessageNo);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 권한 확인 (URL의 studyNo 사용)
            boolean isStudyMember = service.isStudyMember(studyNo, loginMember.getMemberNo());
            if (!isStudyMember) {
                response.put("success", false);
                response.put("error", "접근 권한이 없습니다.");
                return response;
            }
            
            List<Chatting> messages;
            if (lastMessageNo > 0) {
                // 특정 메시지 번호 이후의 새 메시지만 조회
                messages = service.getMessagesAfter(studyNo, lastMessageNo);
            } else {
                // 전체 메시지 조회
                messages = service.getMessagesByStudyNo(studyNo);
            }
            
            Map<Integer, String> memberMap = getMemberMapForStudy(studyNo);
            
            response.put("success", true);
            response.put("messages", messages);
            response.put("memberMap", memberMap);
            response.put("loginMemberNo", loginMember.getMemberNo());
            
            log.info("AJAX 조회 결과 - 메시지 수: {}, 멤버 맵: {}", messages.size(), memberMap);
            
        } catch (Exception e) {
            log.error("AJAX 메시지 조회 중 오류 발생", e);
            response.put("success", false);
            response.put("error", "메시지 조회에 실패했습니다.");
        }
        
        return response;
    }

    @PostMapping("/send")
    public String sendStudyChatMessage(@PathVariable("studyNo") int studyNo,
                                     @RequestParam("content") String content,
                                     @SessionAttribute("loginMember") Member loginMember,
                                     RedirectAttributes redirectAttributes) {

        log.info("메시지 전송 요청 - studyNo: {}, senderNo: {}, content: {}", 
                studyNo, loginMember.getMemberNo(), content);
        
        try {
            // 권한 확인 (URL의 studyNo 사용)
            boolean isStudyMember = service.isStudyMember(studyNo, loginMember.getMemberNo());
            if (!isStudyMember) {
                redirectAttributes.addFlashAttribute("error", "해당 스터디의 멤버만 메시지를 보낼 수 있습니다.");
                return "redirect:/study/" + studyNo + "/chat";
            }
            
            Chatting message = new Chatting();
            message.setStudyNo(studyNo);  // URL의 studyNo 사용
            message.setSenderNo(loginMember.getMemberNo());
            message.setContent(content.trim());

            int result = service.insertMessage(message);
            log.info("메시지 전송 결과: {}", result);
            
            if (result > 0) {
                redirectAttributes.addFlashAttribute("message", "메시지가 전송되었습니다.");
            } else {
                redirectAttributes.addFlashAttribute("error", "메시지 전송에 실패했습니다.");
            }
            
        } catch (Exception e) {
            log.error("메시지 전송 중 오류 발생", e);
            redirectAttributes.addFlashAttribute("error", "메시지 전송 중 오류가 발생했습니다.");
        }
        
        return "redirect:/study/" + studyNo + "/chat";  // URL의 studyNo 사용
    }

    /**
     * 스터디 멤버 정보 매핑 조회
     */
    private Map<Integer, String> getMemberMapForStudy(int studyNo) {
        Map<Integer, String> memberMap = new HashMap<>();
        
        try {
            List<Member> studyMembers = memberService.getStudyMembers(studyNo);
            for (Member member : studyMembers) {
                memberMap.put(member.getMemberNo(), member.getMemberName());
            }
            log.debug("스터디 {} 멤버 정보 매핑 완료: {} 명", studyNo, memberMap.size());
            
        } catch (Exception e) {
            log.error("멤버 정보 조회 중 오류 발생", e);
        }
        
        return memberMap;
    }
}