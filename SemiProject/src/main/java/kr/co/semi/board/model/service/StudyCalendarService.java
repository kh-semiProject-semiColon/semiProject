package kr.co.semi.board.model.service;

import java.util.List;

import kr.co.semi.board.model.dto.Calendar;
import kr.co.semi.member.model.dto.Member;

public interface StudyCalendarService {

	List<Calendar> StudyCalendarList(Member loginMember) throws Exception;
	
	void calendarSave(Calendar vo) throws Exception;

	void calendarDelete(String no) throws Exception;

	void eventUpdate(Calendar vo) throws Exception;

	int bringStudyNo(int memberNo);
}
