package kr.co.semi.chatting.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.semi.chatting.model.dto.Chatting;

@Mapper
public interface ChattingMapper {

	/**
	 * 채팅 메시지 저장
	 */
	int insertMessage(Chatting message);

	/**
	 * 스터디의 모든 메시지 조회 (시간순 정렬)
	 */
	List<Chatting> getMessagesByStudyNo(@Param("studyNo") int studyNo);

	/**
	 * 특정 메시지 번호 이후의 새 메시지들 조회
	 */
	List<Chatting> getMessagesAfter(@Param("studyNo") int studyNo, @Param("lastMessageNo") int lastMessageNo);

	/**
	 * 스터디 멤버 여부 확인
	 */
	boolean isStudyMember(@Param("studyNo") int studyNo, @Param("memberNo") int memberNo);

	/**
	 * 스터디의 최근 메시지 조회 (개수 제한)
	 */
	List<Chatting> getRecentMessages(@Param("studyNo") int studyNo, @Param("limit") int limit);
}