package kr.co.semi.board.model.service;

import java.util.List;

import kr.co.semi.board.model.dto.Calendar;
import kr.co.semi.board.model.dto.StudyCalendar;
import kr.co.semi.member.model.dto.Member;

public interface StudyCalendarService {

	/** 스터디 전용 캘린더 일정 조회
	 * @param loginMember
	 * @return
	 * @throws Exception
	 */
	List<StudyCalendar> StudyCalendarList(Member loginMember) throws Exception;
	
	/** 스터디 전용 캘린더 일정 저장
	 * @param vo
	 * @throws Exception
	 */
	void calendarSave(StudyCalendar vo) throws Exception;

	/** 스터디 전용 캘린더 일정 삭제
	 * @param calendarNo
	 * @throws Exception
	 */
	void calendarDelete(long calendarNo) throws Exception;

	void eventUpdate(StudyCalendar vo) throws Exception;

	/** 해당 멤버의 스터디넘버를 가져옴
	 * @param memberNo
	 * @return
	 */
	int bringStudyNo(int memberNo);
}
