package kr.co.semi.studyboard.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.semi.studyboard.model.mapper.StudyBoardMapper;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class StudyBoardServiceImpl implements StudyBoardService {

	@Autowired
	private StudyBoardMapper mapper;
	
	public List<Map<String, Object>> selectBoardTypeList(){
		return mapper.selectStudyBoardTypeList();
	}
	
	@Override
	public Map<String, Object> searchList(Map<String, Object> paraMap, int cp) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Map<String, Object> selectStudyBoardList(int studyNo, int cp) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
