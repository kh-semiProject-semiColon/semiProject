package kr.co.semi.studyboard.model.service;

import java.util.List;

import kr.co.semi.studyboard.model.dto.StudyComment;

public interface StudyCommentService {

	int insert(StudyComment studyComment);

	int delete(int studyCommentNo);

	List<StudyComment> select(int studyBoardNo);

	int update(StudyComment studyComment);

	
}
