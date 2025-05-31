package kr.co.semi.studyboard.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.semi.studyboard.model.dto.StudyComment;
import kr.co.semi.studyboard.model.mapper.StudyCommentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Transactional(rollbackFor = Exception.class)
@Service
@RequiredArgsConstructor
@Slf4j
public class StudyCommentServiceImpl implements StudyCommentService{

	private final StudyCommentMapper mapper;
	
	@Override
		public List<StudyComment> select(int studyBoardNo) {
			return mapper.select(studyBoardNo);
		}
	
	@Override
	public int delete(int studyCommentNo) {
		return mapper.delete(studyCommentNo);
	}
	
	@Override
	public int insert(StudyComment studyComment) {
		return mapper.insert(studyComment);
	}
	
	@Override
	public int update(StudyComment studyComment) {
		return mapper.update(studyComment);
	}
	
}
