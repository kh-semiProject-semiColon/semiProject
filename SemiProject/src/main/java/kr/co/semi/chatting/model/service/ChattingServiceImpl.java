package kr.co.semi.chatting.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.semi.chatting.model.dto.Chatting;
import kr.co.semi.chatting.model.mapper.ChattingMapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class ChattingServiceImpl implements ChattingService {
	
	@Autowired
	private ChattingMapper mapper;

	@Override
	public int insertMessage(Chatting message) {
		return mapper.insertMessage(message);
	}

	@Override
	public List<Chatting> getMessagesByStudyNo(int studyNo) {
		return mapper.getMessagesByStudyNo(studyNo);
	}

	@Override
	public boolean isStudyMember(int studyNo, int memberNo) {
		return mapper.isStudyMember(studyNo, memberNo);
	}

}
