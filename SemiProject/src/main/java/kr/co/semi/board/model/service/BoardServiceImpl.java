package kr.co.semi.board.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.semi.board.model.dto.Announce;
import kr.co.semi.board.model.dto.Board;
import kr.co.semi.board.model.dto.Pagination;
import kr.co.semi.board.model.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Transactional(rollbackFor = Exception.class)
@Service
@RequiredArgsConstructor
@Slf4j
public class BoardServiceImpl implements BoardService{

	private final BoardMapper mapper;
	
	@Override
		public Map<String, Object> selectAllAnnounce(int cp) {
		// 1. 지정된 게시판(boardCode) 에서 삭제되지 않은 게시글 수를 조회
		// 삭제되지 않은 게시글 수를 조회
		int listCount = mapper.getAnnounceCount();
		
		
		// 2. 1번의 결과 + cp를 이용해서 pagination 객체를 생성
		// Pagination 객체 : 게시글 목록 구성에 필요한 값을 저장한 객체
		Pagination pagination = new Pagination(cp, listCount);
		
		// 3. 특정 게시판의 지정된 페이지 목록 조회
		/*
		 * ROWBOUNDS 객체 (MyBatis 제공 객체)
		 * : 지정된 크기만큼 건너 뛰고(offset)
		 * 제한된 크기만큼의(limit) 행을 조회하는 객체
		 * 
		 * --> 페이징 처리가 굉장히 간단해짐
		 * 
		 * */
		int limit = pagination.getLimit();
		int offset = (cp - 1) * limit;
		RowBounds rowBounds = new RowBounds(offset,limit);
		
		// Mapper 메서드 호출 시 원래 전달할 수 있는 매개변수 1개
		// -> rowBounds를 통해 매개변수 2개 전달 가능
		// 첫번째 매개변수 : SQL에 전달할 파라미터
		// 두번째 매개변수 : RowBounds 객체 전달
		List<Announce> announce = mapper.selectAllAnnounce(rowBounds);
		
		
		// 4. 목록 조회 결과 + Pagination 객체를 Map으로 묵어서 반환
		Map<String, Object> map = new HashMap<>();
		
		map.put("pagination", pagination);
		map.put("announce", announce);

		return map;
		}
	
	@Override
	public Map<String, Object> searchAnnounceList(Map<String, Object> paramMap, int cp) {
		// paramMap(key, query, boardCode)
		
		// 1. 지정된 게시판(boardCode)에서
		// 검색 조건에 맞으면서
		// 삭제되지 않은 게시글 수를 조회

		int listCount = mapper.getAnnounceSearchCount(paramMap);
		
		// 2. 1번의 결과 + cp를 이용해서
		// Pagination 객체를 생성
		Pagination pagination = new Pagination(cp, listCount);
		
		// 3. 특정 게시판의 지정된 페이지 목록 조회
		int limit = pagination.getLimit();
		int offset = (cp - 1) * limit;
		RowBounds rowBounds = new RowBounds(offset,limit);
		
		// mapper 메서드 호출 코드 수행
		// -> Mapper 메서드 호출 시 전달할 수 있는 매개변수 1개
		// -> 2개를 전달할 수 있는 경우가 있음
		// RowBounds를 이용할 때
		// 1번째 : SQL에 전달할 파라미터
		// 2번째 : RowBounds 객체
		// 파라미터로 전달할 값 없을 경우 null이라도 전달

		List<Announce> announce = mapper.selectAnnounceSearchList(paramMap, rowBounds);
		
		// 4. 목록 조회 결과 + Pagination 객체를 Map으로 묶음
		Map<String, Object> map = new HashMap();
		
		map.put("pagination", pagination);
		map.put("announce", announce);
		
		return map;
	}
	
	
		@Override
		public Map<String, Object> selectBoardList(int boardCode, int cp) {
		// 1. 지정된 게시판(boardCode) 에서 삭제되지 않은 게시글 수를 조회
				// 삭제되지 않은 게시글 수를 조회
				int listCount = mapper.getBoardListCount(boardCode);
				
				
				// 2. 1번의 결과 + cp를 이용해서 pagination 객체를 생성
				// Pagination 객체 : 게시글 목록 구성에 필요한 값을 저장한 객체
				Pagination pagination = new Pagination(cp, listCount);
				
				// 3. 특정 게시판의 지정된 페이지 목록 조회
				/*
				 * ROWBOUNDS 객체 (MyBatis 제공 객체)
				 * : 지정된 크기만큼 건너 뛰고(offset)
				 * 제한된 크기만큼의(limit) 행을 조회하는 객체
				 * 
				 * --> 페이징 처리가 굉장히 간단해짐
				 * 
				 * */
				int limit = pagination.getLimit();
				int offset = (cp - 1) * limit;
				RowBounds rowBounds = new RowBounds(offset,limit);
				
				// Mapper 메서드 호출 시 원래 전달할 수 있는 매개변수 1개
				// -> rowBounds를 통해 매개변수 2개 전달 가능
				// 첫번째 매개변수 : SQL에 전달할 파라미터
				// 두번째 매개변수 : RowBounds 객체 전달
				List<Board> boardList = mapper.selectBoardList(boardCode, rowBounds);
				
				
				// 4. 목록 조회 결과 + Pagination 객체를 Map으로 묵어서 반환
				Map<String, Object> map = new HashMap<>();
				
				map.put("pagination", pagination);
				map.put("boardList", boardList);
				
				// 5. 결과 반환
				
				return map;
	}
	
	@Override
	public Map<String, Object> searchList(Map<String, Object> paramMap, int cp) {

		// paramMap(key, query, boardCode)
		
		// 1. 지정된 게시판(boardCode)에서
		// 검색 조건에 맞으면서
		// 삭제되지 않은 게시글 수를 조회
		
		int listCount = mapper.getBoardSearchCount(paramMap);
		
		// 2. 1번의 결과 + cp를 이용해서
		// Pagination 객체를 생성
		Pagination pagination = new Pagination(cp, listCount);
		
		// 3. 특정 게시판의 지정된 페이지 목록 조회
		int limit = pagination.getLimit();
		int offset = (cp - 1) * limit;
		RowBounds rowBounds = new RowBounds(offset,limit);
		
		// mapper 메서드 호출 코드 수행
		// -> Mapper 메서드 호출 시 전달할 수 있는 매개변수 1개
		// -> 2개를 전달할 수 있는 경우가 있음
		// RowBounds를 이용할 때
		// 1번째 : SQL에 전달할 파라미터
		// 2번째 : RowBounds 객체
		// 파라미터로 전달할 값 없을 경우 null이라도 전달
		List<Board> boardList = mapper.selectBoardSearchList(paramMap, rowBounds);
		
		// 4. 목록 조회 결과 + Pagination 객체를 Map으로 묶음
		Map<String, Object> map = new HashMap();
		
		map.put("pagination", pagination);
		map.put("boardList", boardList);
		
		return map;
	}
	
	@Override
	public Board selectOne(Map<String, Integer> map) {
		
		Board result = mapper.selectOne(map);
		
		
		return result;
	}
	
	@Override
	public Announce announceOne(Map<String, Integer> map) {
		return mapper.announceOne(map);
	}
	
	@Override
	public int updateReadCount(int boardNo) {
		
		// 1. 조회수 1 증가 (UPDATE)
		int result = mapper.updateReadCount(boardNo);
		
		// 2. 현재 조회 수 조회
		if(result>0) {
			return mapper.selectReadCount(boardNo);
		}
		
		// 실패한 경우 -1 반환
		return -1;
	}
	
	@Override
	public int updateAnnounceCount(int announceNo) {
		// 1. 조회수 1 증가 (UPDATE)
		int result = mapper.updateAnnounceCount(announceNo);
		
		// 2. 현재 조회 수 조회
		if(result>0) {
			return mapper.selectAnnounceCount(announceNo);
		}
		
		// 실패한 경우 -1 반환
		return -1;
	}
	
	
	@Override
	public int boardLike(Map<String, Integer> map) {
		int result = 0;
		
		// 1. 좋아요가 체크된 상태인 경우 (likeCheck == 1)
		// -> BOARD_LIKE 테이블에 DELETE 실행
		if(map.get("likeCheck") == 1) {
			
			result = mapper.deleteBoardLike(map);
			
		}else {
			// 2. 좋아요가 해제된 상태인 경우 (likeCheck == 0)
			// -> BOARD_LIKE 테이블에 INSERT 실행
			result = mapper.insertBoardLike(map);
			
		}
		
		// 3. 다시 해당 게시글의 좋아요 개수를 조회해서 반환
		if(result > 0) {
			return mapper.selectLikeCount(map.get("boardNo"));
		}
		
		
		return -1; // 좋아요 처리 실패
	}
}
