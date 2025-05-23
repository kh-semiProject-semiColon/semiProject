package kr.co.semi.board.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import kr.co.semi.board.model.dto.HireInfo;

@Mapper
public interface BoardMapper {

	/** 게시글 수 조회
	 * @param hireNo
	 */
	int getListCount();

	/** 지정된 페이지 목록 조회
	 * @param rowBounds
	 * @return
	 */
	List<HireInfo> selecHireBoardList(RowBounds rowBounds);

	
	/** 검색 서비스
	 * @param paramMap
	 * @return
	 */
	int getSerchCount(Map<String, Object> paramMap);

}
