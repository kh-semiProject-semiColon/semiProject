package kr.co.semi.board.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import kr.co.semi.board.model.dto.Board;

@Mapper
public interface EditBoardMapper {

	/** 게시글 작성
	 * @param inputBoard
	 * @return
	 */
	int boardInsert(Board inputBoard);

}
