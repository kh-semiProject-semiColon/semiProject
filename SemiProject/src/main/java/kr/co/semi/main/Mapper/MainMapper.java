package kr.co.semi.main.Mapper;

import org.apache.ibatis.annotations.Mapper;

import kr.co.semi.studyboard.model.dto.Study;

@Mapper
public interface MainMapper {

	/** 스터디 생성 by 김성원
	 * @param study
	 * @return
	 */
	int studyCreation(Study study);

}
