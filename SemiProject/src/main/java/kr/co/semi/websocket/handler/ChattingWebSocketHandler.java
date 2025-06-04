package kr.co.semi.websocket.handler;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;
import kr.co.semi.chatting.model.dto.Chatting;
import kr.co.semi.chatting.model.service.ChattingService;
import kr.co.semi.member.model.dto.Member;
import kr.co.semi.member.model.service.MemberService;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ChattingWebSocketHandler extends TextWebSocketHandler {
    
    @Autowired
    private ChattingService service;
    
    @Autowired
    private MemberService memberService;
    
    // 연결된 모든 세션을 관리
    private Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        
        // 연결된 사용자 정보 로깅
        HttpSession httpSession = (HttpSession) session.getAttributes().get("session");
        if (httpSession != null) {
            Member loginMember = (Member) httpSession.getAttribute("loginMember");
            if (loginMember != null) {
                log.info("WebSocket 연결 성공 - 세션ID: {}, 사용자: {} (memberNo: {})", 
                        session.getId(), loginMember.getMemberName(), loginMember.getMemberNo());
            } else {
                log.warn("WebSocket 연결 - 로그인 정보 없음. 세션ID: {}", session.getId());
            }
        } else {
            log.warn("WebSocket 연결 - HttpSession 없음. 세션ID: {}", session.getId());
        }
    }
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        log.info("WebSocket 연결 해제 - 세션ID: {}, 상태: {}", session.getId(), status);
    }
    
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("메시지 수신: {}", message.getPayload());
        
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Chatting msg = objectMapper.readValue(message.getPayload(), Chatting.class);
            
            // 발신자 정보 확인
            HttpSession senderHttpSession = (HttpSession) session.getAttributes().get("session");
            if (senderHttpSession == null) {
                log.error("발신자 HttpSession이 null입니다.");
                sendErrorMessage(session, "세션이 만료되었습니다.");
                return;
            }
            
            Member senderMember = (Member) senderHttpSession.getAttribute("loginMember");
            if (senderMember == null) {
                log.error("발신자 로그인 정보가 없습니다.");
                sendErrorMessage(session, "로그인이 필요합니다.");
                return;
            }
            
            // 발신자가 해당 스터디 멤버인지 확인
            boolean isSenderStudyMember = service.isStudyMember(msg.getStudyNo(), senderMember.getMemberNo());
            if (!isSenderStudyMember) {
                log.warn("비인가 메시지 전송 시도 - 사용자: {}, 스터디: {}", 
                        senderMember.getMemberNo(), msg.getStudyNo());
                sendErrorMessage(session, "해당 스터디의 멤버만 메시지를 보낼 수 있습니다.");
                return;
            }
            
            // 메시지 유효성 검사
            if (msg.getContent() == null || msg.getContent().trim().isEmpty()) {
                log.warn("빈 메시지 전송 시도");
                sendErrorMessage(session, "메시지 내용을 입력해주세요.");
                return;
            }
            
            // 메시지 내용 정리
            msg.setContent(msg.getContent().trim());
            msg.setSenderNo(senderMember.getMemberNo()); // 보안을 위해 서버에서 설정
            
            log.info("메시지 저장 시도 - 스터디: {}, 발신자: {}, 내용: {}", 
                    msg.getStudyNo(), msg.getSenderNo(), msg.getContent());
            
            // 데이터베이스에 메시지 저장
            int result = service.insertMessage(msg);
            
            if (result > 0) {
                log.info("메시지 저장 성공 - messageNo: {}", msg.getMessageNo());
                
                // 시간 설정
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");
                msg.setSendTime(sdf.format(new Date()));
                
                // 해당 스터디의 모든 멤버에게 메시지 전송 (발신자 정보 포함)
                broadcastToStudyMembers(msg, senderMember, objectMapper);
                
            } else {
                log.error("메시지 저장 실패");
                sendErrorMessage(session, "메시지 전송에 실패했습니다.");
            }
            
        } catch (Exception e) {
            log.error("메시지 처리 중 오류 발생", e);
            sendErrorMessage(session, "메시지 처리 중 오류가 발생했습니다.");
        }
    }
    
    /**
     * 해당 스터디의 모든 멤버에게 메시지 브로드캐스트
     */
    private void broadcastToStudyMembers(Chatting msg, Member senderMember, ObjectMapper objectMapper) {
        int broadcastCount = 0;
        
        synchronized (sessions) {
            for (WebSocketSession s : sessions) {
                try {
                    // 세션이 열려있는지 확인
                    if (!s.isOpen()) {
                        log.debug("닫힌 세션 건너뜀: {}", s.getId());
                        continue;
                    }
                    
                    // HttpSession 가져오기
                    HttpSession httpSession = (HttpSession) s.getAttributes().get("session");
                    if (httpSession == null) {
                        log.debug("HttpSession이 null인 세션 건너뜀: {}", s.getId());
                        continue;
                    }
                    
                    Member loginMember = (Member) httpSession.getAttribute("loginMember");
                    if (loginMember == null) {
                        log.debug("로그인 정보가 없는 세션 건너뜀: {}", s.getId());
                        continue;
                    }
                    
                    // 해당 사용자가 스터디 멤버인지 확인
                    boolean isStudyMember = service.isStudyMember(msg.getStudyNo(), loginMember.getMemberNo());
                    if (isStudyMember) {
                        // 각 수신자별로 메시지 객체 복사 후 발신자 프로필 설정
                        Chatting messageToSend = createMessageCopy(msg);
                        messageToSend.setProfileImg(senderMember.getProfileImg()); // 발신자 프로필 설정
                        
                        String json = objectMapper.writeValueAsString(messageToSend);
                        s.sendMessage(new TextMessage(json));
                        broadcastCount++;
                        
                        log.debug("메시지 전송 성공 - 세션: {}, 수신자: {}, 발신자 프로필: {}", 
                                s.getId(), loginMember.getMemberName(), senderMember.getProfileImg());
                    } else {
                        log.debug("스터디 멤버가 아닌 사용자 건너뜀 - 사용자: {}, 스터디: {}", 
                                loginMember.getMemberNo(), msg.getStudyNo());
                    }
                    
                } catch (Exception e) {
                    log.error("세션 {}에 메시지 전송 실패", s.getId(), e);
                }
            }
        }
        
        log.info("메시지 브로드캐스트 완료 - 스터디: {}, 전송 성공: {}개 세션", 
                msg.getStudyNo(), broadcastCount);
    }
    
    /**
     * 메시지 객체 복사 (깊은 복사)
     */
    private Chatting createMessageCopy(Chatting original) {
        Chatting copy = new Chatting();
        copy.setMessageNo(original.getMessageNo());
        copy.setStudyNo(original.getStudyNo());
        copy.setSenderNo(original.getSenderNo());
        copy.setContent(original.getContent());
        copy.setSendTime(original.getSendTime());
        // profileImg는 각각 다르게 설정되므로 여기서는 복사하지 않음
        return copy;
    }
    
    /**
     * 특정 세션에 오류 메시지 전송
     */
    private void sendErrorMessage(WebSocketSession session, String errorMessage) {
        try {
            if (session.isOpen()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("type", "error");
                errorResponse.put("message", errorMessage);
                
                ObjectMapper objectMapper = new ObjectMapper();
                String json = objectMapper.writeValueAsString(errorResponse);
                session.sendMessage(new TextMessage(json));
            }
        } catch (Exception e) {
            log.error("오류 메시지 전송 실패", e);
        }
    }
}