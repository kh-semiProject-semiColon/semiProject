package kr.co.semi.board.model.service;

import kr.co.semi.board.model.dto.Board;

public interface EditBoardService {

	/** 게시글 내용 작성
	 * @param inputBoard
	 * @return
	 */
	int boardInsert(Board inputBoard);

}
