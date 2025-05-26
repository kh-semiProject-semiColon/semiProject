package kr.co.semi.studyboard.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.semi.studyboard.model.dto.Study;

@Mapper
public interface StudyMapper {

	/** 모든 스터디 조회
	 * @return
	 */
	List<Study> selectAllStudy();

	/** 팀장인 스터디원 조회
	 * @return
	 */
	List<Study> selectCap();


}
