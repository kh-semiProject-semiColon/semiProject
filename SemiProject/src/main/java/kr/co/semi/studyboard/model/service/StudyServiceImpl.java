package kr.co.semi.studyboard.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.semi.studyboard.model.dto.Study;
import kr.co.semi.studyboard.model.mapper.StudyMapper;
import lombok.RequiredArgsConstructor;

@Transactional(rollbackFor = Exception.class)
@Service
@RequiredArgsConstructor
public class StudyServiceImpl implements StudyService{
	
	private final StudyMapper mapper;
	
		// 모든 스터디 조회
	@Override
	public List<Study> selectAllStudy() {
		return mapper.selectAllStudy();
	}
	
	// 스터디 팀장 조회
	@Override
	public List<Study> selectCap() {
		return mapper.selectCap();
	}

	// 스터디 회원 수 조회
	@Override
	public List<Study> countMember() {
		return mapper.countMember();
	}
}
