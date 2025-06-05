package kr.co.semi.studyboard.model.service;

import java.util.HashMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.semi.board.model.dto.Pagination;
import kr.co.semi.studyboard.model.dto.Study;
import kr.co.semi.studyboard.model.mapper.StudyMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Transactional(rollbackFor = Exception.class)
@Service
@RequiredArgsConstructor
@Slf4j
public class StudyServiceImpl implements StudyService{
	
	private final StudyMapper mapper;
	
	// 모든 스터디 조회
	@Override
	public Map<String, Object> selectAllStudy(int cp) {
		
		// 1. 지정된 게시판(boardCode) 에서 삭제되지 않은 게시글 수를 조회
				// 삭제되지 않은 게시글 수를 조회
				int listCount = mapper.getStudyCount();
				
				
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
				List<Study> study = mapper.selectAllStudy(rowBounds);
				
				System.out.println(study);
				
				log.debug("boardList 결과 : {}", pagination.getListCount());
				
				// 4. 목록 조회 결과 + Pagination 객체를 Map으로 묵어서 반환
				Map<String, Object> map = new HashMap<>();
				
				map.put("pagination", pagination);
				map.put("study", study);
		
		return map;
	}
	
	@Override
	public Map<String, Object> searchList(Map<String, Object> paramMap, int cp) {
		// paramMap(key, query, boardCode)
		
				// 1. 지정된 게시판(boardCode)에서
				// 검색 조건에 맞으면서
				// 삭제되지 않은 게시글 수를 조회
		if ("c".equals(paramMap.get("key"))) {

		    String query = (String) paramMap.get("query");



		    if (query.contains("레") || query.contains("벨") || query.contains("업")) {

		        paramMap.put("query", 1);

		    } else if (query.contains("습") || query.contains("복")) {

		        paramMap.put("query", 2);

		    } else if (query.contains("문") || query.contains("이") || query.contains("제") || query.contains("풀")) {

		        paramMap.put("query", 3);

		    } else if (query.contains("자")|| query.contains("격")|| query.contains("증")) {

		        paramMap.put("query", 4);

		    } else if (query.contains("프") || query.contains("로")|| query.contains("젝")|| query.contains("트")) {

		        paramMap.put("query", 5);

		    } else {

		    	paramMap.put("query", 0);

		    }

		}
		
				int listCount = mapper.getSearchCount(paramMap);
				
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
				List<Study> study = mapper.selectSearchList(paramMap, rowBounds);
				
				// 4. 목록 조회 결과 + Pagination 객체를 Map으로 묶음
				Map<String, Object> map = new HashMap();
				
				map.put("pagination", pagination);
				map.put("study", study);
				
				return map;
	}
	
	
	// 스터디 팀장 조회
	@Override
	public List<Study> selectCap() {
		return mapper.selectCap();
	}

	// 메인페이지에서 사용할 스터디 현황 불러오기
	@Override
	public List<Study> selectMainStudy() {
		return mapper.selectMainStudy();
	}

}
