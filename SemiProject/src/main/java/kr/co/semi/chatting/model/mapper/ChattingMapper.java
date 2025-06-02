package kr.co.semi.chatting.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.semi.chatting.model.dto.Chatting;

@Mapper
public interface ChattingMapper {

	int insertMessage(Chatting message);

	List<Chatting> getMessagesByStudyNo(int studyNo);

	boolean isStudyMember(int studyNo, int memberNo);
	

}
