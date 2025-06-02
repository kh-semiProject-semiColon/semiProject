package kr.co.semi.chatting.model.service;

import java.util.List;

import kr.co.semi.chatting.model.dto.Chatting;

public interface ChattingService {

    /**
     * 메시지 저장
     */
    int insertMessage(Chatting message);

    /**
     * 스터디의 모든 메시지 조회 (최근 순)
     */
    List<Chatting> getMessagesByStudyNo(int studyNo);

    /**
     * 특정 메시지 번호 이후의 새 메시지들 조회
     */
    List<Chatting> getMessagesAfter(int studyNo, int lastMessageNo);

    /**
     * 사용자가 스터디 멤버인지 확인
     */
    boolean isStudyMember(int studyNo, int memberNo);

    /**
     * 스터디의 최근 메시지 조회 (개수 제한)
     */
    List<Chatting> getRecentMessages(int studyNo, int limit);
}