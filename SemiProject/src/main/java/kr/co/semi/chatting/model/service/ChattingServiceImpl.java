package kr.co.semi.chatting.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.semi.chatting.model.dto.Chatting;
import kr.co.semi.chatting.model.mapper.ChattingMapper;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class ChattingServiceImpl implements ChattingService {
	
	@Autowired
	private ChattingMapper mapper;

	@Override
	public int insertMessage(Chatting message) {
		try {
			// 메시지 유효성 검증
			if (message == null || message.getContent() == null || 
				message.getContent().trim().isEmpty() || 
				message.getStudyNo() <= 0 || message.getSenderNo() <= 0) {
				log.warn("유효하지 않은 메시지 데이터: {}", message);
				return 0;
			}
			
			// 내용 트림 처리
			message.setContent(message.getContent().trim());
			
			int result = mapper.insertMessage(message);
			log.info("메시지 저장 완료 - studyNo: {}, senderNo: {}, result: {}", 
					message.getStudyNo(), message.getSenderNo(), result);
			
			return result;
		} catch (Exception e) {
			log.error("메시지 저장 중 오류 발생", e);
			throw e; // 트랜잭션 롤백을 위해 예외 재발생
		}
	}

	@Override
	public List<Chatting> getMessagesByStudyNo(int studyNo) {
		try {
			if (studyNo <= 0) {
				log.warn("유효하지 않은 studyNo: {}", studyNo);
				return List.of(); // 빈 리스트 반환
			}
			
			List<Chatting> messages = mapper.getMessagesByStudyNo(studyNo);
			log.debug("스터디 {} 메시지 조회 완료: {} 개", studyNo, messages.size());
			
			return messages;
		} catch (Exception e) {
			log.error("메시지 목록 조회 중 오류 발생 - studyNo: {}", studyNo, e);
			return List.of(); // 오류 시 빈 리스트 반환
		}
	}

	@Override
	public List<Chatting> getMessagesAfter(int studyNo, int lastMessageNo) {
		try {
			if (studyNo <= 0 || lastMessageNo < 0) {
				log.warn("유효하지 않은 파라미터 - studyNo: {}, lastMessageNo: {}", studyNo, lastMessageNo);
				return List.of();
			}
			
			List<Chatting> messages = mapper.getMessagesAfter(studyNo, lastMessageNo);
			log.debug("스터디 {} 신규 메시지 조회 완료: {} 개 (after: {})", studyNo, messages.size(), lastMessageNo);
			
			return messages;
		} catch (Exception e) {
			log.error("신규 메시지 조회 중 오류 발생 - studyNo: {}, lastMessageNo: {}", studyNo, lastMessageNo, e);
			return List.of();
		}
	}

	@Override
	public boolean isStudyMember(int studyNo, int memberNo) {
		try {
			if (studyNo <= 0 || memberNo <= 0) {
				log.warn("유효하지 않은 파라미터 - studyNo: {}, memberNo: {}", studyNo, memberNo);
				return false;
			}
			
			boolean result = mapper.isStudyMember(studyNo, memberNo);
			log.debug("스터디 멤버 확인 - studyNo: {}, memberNo: {}, result: {}", studyNo, memberNo, result);
			
			return result;
		} catch (Exception e) {
			log.error("스터디 멤버 확인 중 오류 발생 - studyNo: {}, memberNo: {}", studyNo, memberNo, e);
			return false; // 오류 시 false 반환 (안전한 기본값)
		}
	}

	@Override
	public List<Chatting> getRecentMessages(int studyNo, int limit) {
		try {
			if (studyNo <= 0 || limit <= 0) {
				log.warn("유효하지 않은 파라미터 - studyNo: {}, limit: {}", studyNo, limit);
				return List.of();
			}
			
			List<Chatting> messages = mapper.getRecentMessages(studyNo, limit);
			log.debug("스터디 {} 최근 메시지 조회 완료: {} 개 (limit: {})", studyNo, messages.size(), limit);
			
			return messages;
		} catch (Exception e) {
			log.error("최근 메시지 조회 중 오류 발생 - studyNo: {}, limit: {}", studyNo, limit, e);
			return List.of();
		}
	}
}