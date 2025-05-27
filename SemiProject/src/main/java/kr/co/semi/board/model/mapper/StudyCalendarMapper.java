package kr.co.semi.board.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.semi.board.model.dto.StudyCalendar;
import kr.co.semi.member.model.dto.Member;

@Mapper
public interface StudyCalendarMapper {

	List<StudyCalendar> StudyCalendarList(Member loginMember);

	void calendarSave(StudyCalendar vo);

	void calendarDelete(long no);

	void eventUpdate(StudyCalendar vo);

	Integer bringStudyNo(int memberNo);
}
