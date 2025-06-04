package kr.co.semi.websocket.handler;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TestWebSocketHandler extends TextWebSocketHandler {
	
	@Autowired
	private ChattingService service;
	
	// 연결된 모든 웹소켓 세션을 저장
	private Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		sessions.add(session);
		log.info("웹소켓 연결 성공: {}", session.getId());
		
		// 연결된 사용자 정보 로깅
		HttpSession httpSession = (HttpSession) session.getAttributes().get("session");
		if (httpSession != null) {
			Member loginMember = (Member) httpSession.getAttribute("loginMember");
			if (loginMember != null) {
				log.info("사용자 {}({})가 채팅에 연결됨", loginMember.getMemberName(), loginMember.getMemberNo());
			}
		}
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		sessions.remove(session);
		log.info("웹소켓 연결 종료: {}, 상태: {}", session.getId(), status);
	}
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		try {
			log.info("수신된 메시지: {}", message.getPayload());
			
			// JSON 파싱
			ObjectMapper objectMapper = new ObjectMapper();
			Chatting msg = objectMapper.readValue(message.getPayload(), Chatting.class);
			
			// 메시지 유효성 검증
			if (msg.getStudyNo() <= 0 || msg.getSenderNo() <= 0 || 
				msg.getContent() == null || msg.getContent().trim().isEmpty()) {
				log.warn("유효하지 않은 메시지: {}", msg);
				return;
			}
			
			// DB에 메시지 저장
			int result = service.insertMessage(msg);
			log.info("메시지 저장 결과: {}", result);
			
			if (result > 0) {
				// 시간 포맷 설정
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");
				msg.setSendTime(sdf.format(new Date()));
				
				// 저장된 메시지의 번호를 다시 조회하여 설정 (필요한 경우)
				// msg.setMessageNo(저장된메시지번호);
				
				// 해당 스터디의 모든 멤버에게 메시지 전송
				broadcastToStudyMembers(msg, objectMapper);
				
			} else {
				log.error("메시지 저장 실패");
				// 실패 시 발신자에게 오류 메시지 전송
				sendErrorMessage(session, "메시지 저장에 실패했습니다.");
			}
			
		} catch (Exception e) {
			log.error("메시지 처리 중 오류 발생", e);
			sendErrorMessage(session, "메시지 처리 중 오류가 발생했습니다.");
		}
	}
	
	/**
	 * 스터디 멤버들에게 메시지 브로드캐스트
	 */
	private void broadcastToStudyMembers(Chatting msg, ObjectMapper objectMapper) {
		String jsonMessage;
		try {
			jsonMessage = objectMapper.writeValueAsString(msg);
		} catch (Exception e) {
			log.error("메시지 JSON 변환 실패", e);
			return;
		}
		
		int successCount = 0;
		int totalSessions = sessions.size();
		
		// 모든 연결된 세션에 대해 확인
		for (WebSocketSession s : sessions) {
			try {
				// 세션이 열려있는지 확인
				if (!s.isOpen()) {
					continue;
				}
				
				HttpSession httpSession = (HttpSession) s.getAttributes().get("session");
				if (httpSession == null) {
					continue;
				}
				
				Member loginMember = (Member) httpSession.getAttribute("loginMember");
				if (loginMember == null) {
					continue;
				}
				
				int memberNo = loginMember.getMemberNo();
				
				// 해당 멤버가 이 스터디의 멤버인지 확인
				boolean isStudyMember = service.isStudyMember(msg.getStudyNo(), memberNo);
				
				if (isStudyMember) {
					s.sendMessage(new TextMessage(jsonMessage));
					successCount++;
					log.debug("메시지 전송 성공: 사용자 {}", memberNo);
				}
				
			} catch (Exception e) {
				log.error("개별 세션 메시지 전송 실패: {}", s.getId(), e);
			}
		}
		
		log.info("메시지 브로드캐스트 완료: 성공 {}/{} (전체 세션: {})", 
				successCount, sessions.size(), totalSessions);
	}
	
	/**
	 * 특정 세션에 오류 메시지 전송
	 */
	private void sendErrorMessage(WebSocketSession session, String errorMsg) {
		try {
			if (session.isOpen()) {
				ObjectMapper objectMapper = new ObjectMapper();
				String errorJson = objectMapper.writeValueAsString(
					new ErrorMessage("error", errorMsg)
				);
				session.sendMessage(new TextMessage(errorJson));
			}
		} catch (Exception e) {
			log.error("오류 메시지 전송 실패", e);
		}
	}
	
	/**
	 * 에러 메시지를 위한 내부 클래스
	 */
	private static class ErrorMessage {
		private String type;
		private String message;
		
		public ErrorMessage(String type, String message) {
			this.type = type;
			this.message = message;
		}
		
		public String getType() { return type; }
		public void setType(String type) { this.type = type; }
		public String getMessage() { return message; }
		public void setMessage(String message) { this.message = message; }
	}
	
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		log.error("웹소켓 전송 오류: {}", session.getId(), exception);
		super.handleTransportError(session, exception);
	}
}