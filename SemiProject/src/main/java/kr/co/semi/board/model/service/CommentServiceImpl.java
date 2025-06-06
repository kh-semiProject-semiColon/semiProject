package kr.co.semi.board.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.semi.board.model.dto.Comment;
import kr.co.semi.board.model.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Transactional(rollbackFor = Exception.class)
@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService{

	private final CommentMapper mapper;
	
	
	// 댓글 목록 조회 서비스
	@Override
	public List<Comment> select(int boardNo) {
		return mapper.select(boardNo);
	}

	// 댓글 답글 등록 서비스
	@Override
	public int insert(Comment comment) {
		return mapper.insert(comment);
	}
	
	// 댓글 삭제 서비스
	@Override
	public int delete(int commentNo) {
		return mapper.delete(commentNo);
	}
	
	// 댓글 수정 서비스
	@Override
	public int update(Comment comment) {
		return mapper.update(comment);
	}
}
