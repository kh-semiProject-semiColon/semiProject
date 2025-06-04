package kr.co.semi.board.model.service;

import java.util.List;

import kr.co.semi.board.model.dto.Comment;
import kr.co.semi.board.model.dto.HireComment;

public interface HireCommentService {

	/** 코멘트 가져오기
	 * @param boardNo
	 * @return
	 */
	List<HireComment> select(HireComment hireComment);

	/** 댓글 작성
	 * @param hireComment
	 * @return
	 */
	int insert(HireComment hireComment);

	/** 댓글 삭제
	 * @param commentNo
	 * @return
	 */
	int delete(int hireCommentNo);

	/** 댓글 수정
	 * @param hireComment
	 * @return
	 */
	int update(HireComment hireComment);

	int selectCurrentCount(int studyNo);

	int selectMaxCount(int studyNo);

}
