package kr.co.semi.board.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.semi.board.model.dto.Calendar;
import kr.co.semi.board.model.mapper.StudyCalendarMapper;
import kr.co.semi.member.model.dto.Member;

@Service
@Transactional(rollbackFor = Exception.class)
public class StudyCalendarServiceImpl implements StudyCalendarService {
	
	@Autowired
	private StudyCalendarMapper mapper;
	
	@Override
	public List<Calendar> StudyCalendarList(Member loginMember) throws Exception {

		return mapper.StudyCalendarList(loginMember);
	}
	

    @Override
    public void calendarSave(Calendar vo) throws Exception {
        mapper.calendarSave(vo);
    }

    @Override
    public void calendarDelete(String no) throws Exception {
        mapper.calendarDelete(no);
    }

    @Override
    public void eventUpdate(Calendar vo) throws Exception {
        mapper.eventUpdate(vo);
    }
    
    @Override
    public int bringStudyNo(int memberNo) {
    	return mapper.bringStudyNo(memberNo);
    }
}
