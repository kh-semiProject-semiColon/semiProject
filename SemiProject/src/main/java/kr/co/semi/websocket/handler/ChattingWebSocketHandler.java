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

@Component // Beam 등록
@Slf4j
public class ChattingWebSocketHandler extends TextWebSocketHandler {
	
	@Autowired
	private ChattingService service;
	
	private Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		sessions.add(session);
		log.info("{} 연결됨",session.getId());
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		sessions.remove(session);
		log.info("{} 연결끊김", session.getId());
	}
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

		// message - JS를 통해 클라이언트로부터 전달 받은 내용
		// {"senderNo": loginMemberNo,
		//	"targetNo": selectTargetNo,
		//	"chattingRoomNo": selectChattingNo,
		//	"messageContent": inputChatting.value}
		
		// Jackson(JSON과 관련된 모듈)에서 제공하는 객체
		ObjectMapper objectMapper = new ObjectMapper();
        Chatting msg = objectMapper.readValue(message.getPayload(), Chatting.class);

        // DB 저장
        int result = service.insertMessage(msg); // 스터디 채팅용 저장 로직

        if (result > 0) {
            // 시간 포맷 세팅
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");
            msg.setSendTime(sdf.format(new Date()));

            // 모든 세션 순회
            for (WebSocketSession s : sessions) {

                HttpSession httpSession = (HttpSession) s.getAttributes().get("session");
                Member loginMember = (Member) httpSession.getAttribute("loginMember");
                int memberNo = loginMember.getMemberNo();

                // 이 회원이 해당 스터디(studyNo)의 멤버인지 확인
                boolean isStudyMember = service.isStudyMember(msg.getStudyNo(), memberNo);

                if (isStudyMember) {
                    String json = objectMapper.writeValueAsString(msg);
                    s.sendMessage(new TextMessage(json));
                }
            }
        }
	}
	}
