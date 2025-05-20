package kr.co.semi.board.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.semi.board.model.dto.Calendar;
import kr.co.semi.board.model.mapper.CalendarMapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class CalendarServiceImpl implements CalendarService {
	
	@Autowired
    private CalendarMapper mapper;

    @Override
    public List<Calendar> calendarList() throws Exception {
    	return mapper.calendarList();
    }

    @Override
    public void calendarSave(Calendar vo) throws Exception {
        mapper.calendarSave(vo);
    }

    @Override
    public void calendarDelete(long calendarNo) throws Exception {
        mapper.calendarDelete(calendarNo);
    }

    @Override
    public void eventUpdate(Calendar vo) throws Exception {
        mapper.eventUpdate(vo);
    }
}
