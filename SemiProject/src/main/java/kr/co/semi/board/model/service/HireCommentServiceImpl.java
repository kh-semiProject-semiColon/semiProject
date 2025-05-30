package kr.co.semi.board.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.semi.board.model.dto.Comment;
import kr.co.semi.board.model.dto.HireComment;
import kr.co.semi.board.model.mapper.HireCommentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Transactional(rollbackFor = Exception.class)
@Service
@RequiredArgsConstructor
@Slf4j
public class HireCommentServiceImpl implements HireCommentService{

	private final HireCommentMapper mapper;
	
	
	// 댓글 목록 조회 서비스
	@Override
	public List<HireComment> select(int hireNo) {
		return mapper.select(hireNo);
	}

	// 댓글 답글 등록 서비스
	@Override
	public int insert(HireComment hireComment) {
		return mapper.insert(hireComment);
	}
	
	// 댓글 삭제 서비스
	@Override
	public int delete(int hireCommentNo) {
		return mapper.delete(hireCommentNo);
	}
	
	// 댓글 수정 서비스
	@Override
	public int update(HireComment hireComment) {
		return mapper.update(hireComment);
	}
}
