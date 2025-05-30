package kr.co.semi.board.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.semi.board.model.dto.HireInfo;
import kr.co.semi.board.model.dto.Pagination;
import kr.co.semi.board.model.mapper.BoardMapper;
import kr.co.semi.board.model.mapper.HireBoardMapper;
import kr.co.semi.member.model.dto.Member;
import kr.co.semi.studyboard.model.dto.Study;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class HireBoardServiceImpl implements HireBoardService{
	
	@Autowired
	private HireBoardMapper mapper;

	// 구인 게시글 조회
	@Override
	public Map<String, Object> showHireBoard(int cp) {

		int listCount = mapper.getListCount();
		
		Pagination pagination = new Pagination(cp, listCount);
		
		int limit = pagination.getLimit();
		int offset = (cp - 1) * limit;
		RowBounds rowBounds = new RowBounds(offset, limit);
		
		List<HireInfo> hireList = mapper.selecHireBoardList(rowBounds);
		
		Map<String, Object> map = new HashMap<>();
		
		map.put("pagination", pagination);
		map.put("hireList", hireList);
		
		
		return map;
	}

	// 검색 서비스
	@Override
	public Map<String, Object> searchList(Map<String, Object> paramMap, int cp) {
		
		int listCount = mapper.getSerchCount(paramMap);
		
		Pagination pagination = new Pagination(cp, listCount);
		
		int limit = pagination.getLimit();
		int offset = (cp - 1) * limit;
		RowBounds rowBounds = new RowBounds(offset, limit);
		
		List<HireInfo> hireList = mapper.selecHireBoardList(rowBounds);
		
		Map<String, Object> map = new HashMap<>();
		
		map.put("pagination", pagination);
		map.put("hireList", hireList);
		
		return map;
	}
	
	// 가입한 스터디 조회
	@Override
	public List<Study> showStudySelect(int memberNo) {
		
		return mapper.showStudySelect(memberNo);
	}

	// 구인 게시글 작성
	@Override
	public int writeHireBoardInsert(HireInfo inputHire) {
		int result = mapper.writeHireBoardInsert(inputHire);

	    if (result > 0) {
	        return inputHire.getHireNo(); // <selectKey>로 세팅된 값 반환
	    } else {
	        return 0;
	    }
	}
	
	// 구인 게시글 상세 조회
	@Override
	public HireInfo selectOne(Map<String, Integer> map) {
		return mapper.selectOne(map);
	}
	
	// 구인 게시글 조회수 증가
	@Override
	public int updateHireReadCount(int hireNo) {
		
		return mapper.updateHireReadCount(hireNo);
		
	}
	
	// 구인 게시글 스터디 조회
	@Override
	public Study selectStudyNo(int studyNo) {
		return mapper.selectStudyNo(studyNo);
	}
	
	// 구인 게시글 업데이트
	@Override
	public int hireUpdate(HireInfo inputHire) {

		// 1. 게시글 부분(제목/내용) 수정
		int result = mapper.hireUpdate(inputHire);
		
		// 수정 실패 시 바로 리턴
		if(result == 0) return 0;
		
		return result;
	}
	
	// 구인 게시글 삭제
	@Override
	public int hireDelete(Map<String, Integer> map) {
		return mapper.hireDelete(map);
	}
	
	@Override
	public int getStudyNo(int memberNo) {
		return mapper.getStudyNo(memberNo);
	}
}
