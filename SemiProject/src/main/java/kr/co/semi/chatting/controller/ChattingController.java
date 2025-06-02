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
    private MemberService memberService; // 멤버 정보 조회용

    /**
     * 채팅 페이지 보기 (HTML 페이지 반환)
     */
    @GetMapping("")
    public String showChatPage(@PathVariable("studyNo") int studyNo, 
                              Model model,
                              @SessionAttribute("loginMember") Member loginMember) {
        
        log.info("채팅 페이지 접근 - studyNo: {}, loginMember: {}", studyNo, loginMember.getMemberNo());
        
        try {
            // 메시지 목록 조회
            List<Chatting> messages = service.getMessagesByStudyNo(studyNo);
            log.info("조회된 메시지 수: {}", messages != null ? messages.size() : 0);
            
            // 멤버 정보 매핑 (사용자 이름 표시용)
            Map<Integer, String> memberMap = getMemberMapForStudy(studyNo);
            
            // 모델에 데이터 추가
            model.addAttribute("messages", messages);
            model.addAttribute("studyNo", studyNo);
            model.addAttribute("loginMember", loginMember);
            model.addAttribute("memberMap", memberMap);
            
            return "study/chat"; // 채팅 페이지 템플릿
            
        } catch (Exception e) {
            log.error("채팅 페이지 로드 중 오류 발생", e);
            model.addAttribute("error", "채팅을 불러오는데 실패했습니다.");
            return "error/500";
        }
    }

    /**
     * 스터디 채팅 메시지 목록 조회 (AJAX용 - JSON 반환)
     */
    @GetMapping("/messages")
    @ResponseBody
    public Map<String, Object> getStudyChatMessages(@PathVariable("studyNo") int studyNo,
                                                   @SessionAttribute("loginMember") Member loginMember) {
        
        log.info("AJAX 메시지 목록 조회 요청 - studyNo: {}", studyNo);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Chatting> messages = service.getMessagesByStudyNo(studyNo);
            Map<Integer, String> memberMap = getMemberMapForStudy(studyNo);
            
            response.put("success", true);
            response.put("messages", messages);
            response.put("memberMap", memberMap);
            response.put("loginMemberNo", loginMember.getMemberNo());
            
            log.info("AJAX 조회 결과 - 메시지 수: {}", messages.size());
            
        } catch (Exception e) {
            log.error("AJAX 메시지 조회 중 오류 발생", e);
            response.put("success", false);
            response.put("error", "메시지 조회에 실패했습니다.");
        }
        
        return response;
    }

    /**
     * 스터디 채팅 메시지 전송
     */
    @PostMapping("/send")
    public String sendStudyChatMessage(@PathVariable("studyNo") int studyNo,
                                     @RequestParam("content") String content,
                                     @SessionAttribute("loginMember") Member loginMember,
                                     RedirectAttributes redirectAttributes) {

        log.info("메시지 전송 요청 - studyNo: {}, senderNo: {}, content: {}", 
                studyNo, loginMember.getMemberNo(), content);
        
        try {
            Chatting message = new Chatting();
            message.setStudyNo(loginMember.getStudyNo());
            message.setSenderNo(loginMember.getMemberNo());
            message.setContent(content);

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
        
        return "redirect:/study/" + studyNo + "/chat";
    }

    /**
     * AJAX로 메시지 전송 (페이지 리로드 없이)
     */
    @PostMapping("/send-ajax")
    @ResponseBody
    public Map<String, Object> sendMessageAjax(@PathVariable("studyNo") int studyNo,
                                             @RequestParam("content") String content,
                                             @SessionAttribute("loginMember") Member loginMember) {

        Map<String, Object> response = new HashMap<>();
        
        try {
            Chatting message = new Chatting();
            message.setStudyNo(studyNo);
            message.setSenderNo(loginMember.getMemberNo());
            message.setContent(content);

            int result = service.insertMessage(message);
            
            if (result > 0) {
                response.put("success", true);
                response.put("message", "메시지가 전송되었습니다.");
            } else {
                response.put("success", false);
                response.put("error", "메시지 전송에 실패했습니다.");
            }
            
        } catch (Exception e) {
            log.error("AJAX 메시지 전송 중 오류 발생", e);
            response.put("success", false);
            response.put("error", "메시지 전송 중 오류가 발생했습니다.");
        }
        
        return response;
    }


    /**
     * 스터디 멤버 정보 매핑 조회 (임시 구현)
     */
    private Map<Integer, String> getMemberMapForStudy(int studyNo) {
        // 실제로는 스터디 멤버 정보를 조회해야 합니다
        Map<Integer, String> memberMap = new HashMap<>();
        
        try {
            
             List<Member> studyMembers = memberService.getStudyMembers(studyNo);
             for (Member member : studyMembers) {
                 memberMap.put(member.getMemberNo(), member.getMemberName());
             }
            
        } catch (Exception e) {
            log.error("멤버 정보 조회 중 오류 발생", e);
        }
        
        return memberMap;
    }

}
