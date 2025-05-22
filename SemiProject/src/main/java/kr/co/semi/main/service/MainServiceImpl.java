package kr.co.semi.main.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.semi.main.Mapper.MainMapper;
import kr.co.semi.studyboard.model.dto.Study;

@Service
@Transactional(rollbackFor = Exception.class)
public class MainServiceImpl implements MainService {
	
	@Autowired
	private MainMapper mapper;

	/** 스터디 생성 서비스
	 * by 김성원
	 */
	@Override
	public int studyCreation(Study study) {
		
		int result = mapper.studyCreation(study);
		return result;
	}
	
	

}
