package kr.co.semi.board.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.semi.board.model.dto.Calendar;
import kr.co.semi.member.model.dto.Member;

@Mapper
public interface StudyCalendarMapper {

	List<Calendar> StudyCalendarList(Member loginMember);

	void calendarSave(Calendar vo);

	void calendarDelete(long no);

	void eventUpdate(Calendar vo);

	Integer bringStudyNo(int memberNo);
}
