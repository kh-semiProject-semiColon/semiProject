package kr.co.semi.board.model.service;

import java.util.List;

import kr.co.semi.board.model.dto.Calendar;

public interface CalendarService {
	
	List<Calendar> calendarList() throws Exception;

	void calendarSave(Calendar vo) throws Exception;

	void calendarDelete(String no) throws Exception;

	void eventUpdate(Calendar vo) throws Exception;
}
