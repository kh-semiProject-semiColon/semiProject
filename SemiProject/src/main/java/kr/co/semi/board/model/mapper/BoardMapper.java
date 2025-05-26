package kr.co.semi.board.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import kr.co.semi.board.model.dto.Announce;
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

	/** 삭제되지 않은 공지사항 게시글 개수
	 * @return
	 */
	int getAnnounceCount();

	/** 모든 공지 가져오기
	 * @param rowBounds
	 * @return
	 */
	List<Announce> selectAllAnnounce(RowBounds rowBounds);

	/** 검색 조건을 포함한 삭제되지 않은 공지글 개수
	 * @param paramMap
	 * @return
	 */
	int getSearchCount(Map<String, Object> paramMap);


}
