package kr.co.semi.studyboard.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StudyBoardMapper {

	List<Map<String, Object>> selectStudyBoardTypeList();


}
