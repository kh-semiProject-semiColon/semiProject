package kr.co.semi.board.model.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.semi.board.model.dto.Board;

@Mapper
public interface EditBoardMapper {

	/** 게시글 작성
	 * @param inputBoard
	 * @return
	 */
	int boardInsert(Board inputBoard);

	/** 게시글 수정
	 * @param inputBoard
	 * @return
	 */
	int boardUpdate(Board inputBoard);

	/** 게시글 삭제
	 * @param map
	 * @return
	 */
	int boardDelete(Map<String, Integer> map);

}
