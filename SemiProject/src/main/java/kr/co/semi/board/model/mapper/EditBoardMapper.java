package kr.co.semi.board.model.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.semi.board.model.dto.Announce;
import kr.co.semi.board.model.dto.Board;
import kr.co.semi.studyboard.model.dto.StudyBoard;

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

	/** 스터디 글 작성
	 * @param inputStudyBoard
	 * @return
	 */
	int studyBoardInsert(StudyBoard inputStudyBoard);

	int studyBoardUpdate(StudyBoard inputStudyBoard);

	int studyBoardDelete(Map<String, Integer> map);

}
