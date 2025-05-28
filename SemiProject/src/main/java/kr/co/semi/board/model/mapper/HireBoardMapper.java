package kr.co.semi.board.model.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.semi.board.model.dto.HireInfo;

@Mapper
public interface HireBoardMapper {

	/** 구인 게시글 상세 조회
	 * @param map
	 * @return
	 */
	HireInfo selectOne(Map<String, Integer> map);

	
	
}
