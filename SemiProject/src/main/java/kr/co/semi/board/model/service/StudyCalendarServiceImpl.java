package kr.co.semi.board.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.semi.board.model.dto.StudyCalendar;
import kr.co.semi.board.model.mapper.StudyCalendarMapper;
import kr.co.semi.member.model.dto.Member;

@Service
@Transactional(rollbackFor = Exception.class)
public class StudyCalendarServiceImpl implements StudyCalendarService {
	
	@Autowired
	private StudyCalendarMapper mapper;
	
	@Override
	public List<StudyCalendar> StudyCalendarList(Member loginMember) throws Exception {

		return mapper.StudyCalendarList(loginMember);
	}
	

    @Override
    public void calendarSave(StudyCalendar vo) throws Exception {
        mapper.calendarSave(vo);
    }

    @Override
    public void calendarDelete(long no) throws Exception {
        mapper.calendarDelete(no);
    }

    @Override
    public void eventUpdate(StudyCalendar vo) throws Exception {
        mapper.eventUpdate(vo);
    }
    
    /**
     * 해당 멤버의 스터디 넘버를 가져옴
     */
    @Override
    public int bringStudyNo(int memberNo) {
    	Integer studyNo = mapper.bringStudyNo(memberNo);
        return studyNo != null ? studyNo : 0;
    }
}
