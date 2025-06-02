package kr.co.semi.chatting.model.service;

import java.util.List;

import kr.co.semi.chatting.model.dto.Chatting;

public interface ChattingService {


	int insertMessage(Chatting message);

	List<Chatting> getMessagesByStudyNo(int studyNo);

	boolean isStudyMember(int studyNo, int memberNo);

}
