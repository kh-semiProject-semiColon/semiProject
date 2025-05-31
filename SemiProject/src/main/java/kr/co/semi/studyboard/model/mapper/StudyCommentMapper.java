package kr.co.semi.studyboard.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.semi.studyboard.model.dto.StudyComment;

@Mapper
public interface StudyCommentMapper {

	List<StudyComment> select(int studyBoardNo);

	int delete(int studyCommentNo);

	int insert(StudyComment studyComment);

	int update(StudyComment studyComment);

}
