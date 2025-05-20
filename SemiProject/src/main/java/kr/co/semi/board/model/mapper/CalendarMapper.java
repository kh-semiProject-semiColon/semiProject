package kr.co.semi.board.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.semi.board.model.dto.Calendar;

@Mapper
public interface CalendarMapper {
	
	List<Calendar> calendarList();

	void calendarSave(Calendar vo);

	void calendarDelete(String no);

	void eventUpdate(Calendar vo);
}
