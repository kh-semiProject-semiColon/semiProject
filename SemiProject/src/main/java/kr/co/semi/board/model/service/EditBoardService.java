package kr.co.semi.board.model.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import kr.co.semi.board.model.dto.Announce;
import kr.co.semi.board.model.dto.Board;
import kr.co.semi.board.model.dto.BoardImg;

public interface EditBoardService {

	/** 게시글 내용 작성
	 * @param inputBoard
	 * @return
	 */
	int boardInsert(Board inputBoard);

	/** 게시글 삭제
	 * @param map
	 * @return
	 */
	int boardDelete(Map<String, Integer> map);

	/** 게시글 수정
	 * @param inputBoard
	 * @param deleteOrderList
	 * @return
	 */
	int boardUpdate(Board inputBoard) throws Exception;

	/** 공지글 삭제
	 * @param map
	 * @return
	 */
	int announceDelete(Map<String, Integer> map);

	/** 공지글 수정
	 * @param inputAnnounce
	 * @return
	 */
	int announceUpdate(Announce inputAnnounce);

	/** 공지글 작성
	 * @param inputAnnounce
	 * @return
	 */
	int announceInsert(Announce inputAnnounce);




}
