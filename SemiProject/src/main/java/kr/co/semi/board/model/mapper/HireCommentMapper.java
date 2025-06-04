package kr.co.semi.board.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.semi.board.model.dto.Comment;
import kr.co.semi.board.model.dto.HireComment;

@Mapper
public interface HireCommentMapper {

	/** 댓글 목록 조회 서비스
	 * @param boardNo
	 * @return
	 */
	List<HireComment> select(HireComment hireComment);

	/** 댓글/ 답글 등록 서비스
	 * @param hireComment
	 * @return
	 */
	int insert(HireComment hireComment);

	/** 댓글 삭제 서비스
	 * @param commentNo
	 * @return
	 */
	int delete(int hireCommentNo);

	/** 댓글 수정 서비스
	 * @param hireComment
	 * @return
	 */
	int update(HireComment hireComment);

	int selectMaxCount(int studyNo);

	int selectCurrentCount(int studyNo);
}
