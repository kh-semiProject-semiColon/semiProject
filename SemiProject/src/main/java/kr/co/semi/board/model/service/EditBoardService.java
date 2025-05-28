package kr.co.semi.board.model.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import kr.co.semi.board.model.dto.Board;
import kr.co.semi.board.model.dto.BoardImg;

public interface EditBoardService {

	/** 게시글 내용 작성
	 * @param inputBoard
	 * @return
	 */
	int boardInsert(Board inputBoard);



}
