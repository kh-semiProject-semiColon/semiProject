package kr.co.semi.board.model.service;

import java.util.List;

import kr.co.semi.board.model.dto.Comment;

public interface CommentService {

	/** 코멘트 가져오기
	 * @param boardNo
	 * @return
	 */
	List<Comment> select(int boardNo);

	/** 댓글 작성
	 * @param comment
	 * @return
	 */
	int insert(Comment comment);

	/** 댓글 삭제
	 * @param commentNo
	 * @return
	 */
	int delete(int commentNo);

	/** 댓글 수정
	 * @param comment
	 * @return
	 */
	int update(Comment comment);

}
