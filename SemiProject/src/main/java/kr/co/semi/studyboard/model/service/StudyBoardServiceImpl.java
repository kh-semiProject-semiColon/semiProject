package kr.co.semi.studyboard.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.semi.studyboard.model.dto.Study;
import kr.co.semi.studyboard.model.dto.StudyBoard;
import kr.co.semi.studyboard.model.mapper.StudyBoardMapper;
import lombok.RequiredArgsConstructor;

/**
 * 🎯 StudyBoardServiceImpl
 * 📌 게시글 관련 비즈니스 로직을 처리하는 서비스 구현체
 * 🔗 Controller → ServiceImpl → Mapper 구조 연결
 */
@Service()
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class StudyBoardServiceImpl implements StudyBoardService {

    private final StudyBoardMapper mapper;

    /**
     * 📋 게시글 목록 조회
     * - 검색 조건, 정렬 조건, 페이지 번호에 따라 게시글 리스트를 반환
     *
     * @param studyNo  현재 선택된 스터디 번호
     * @param key      검색 키 (예: 제목, 작성자)
     * @param keyword  검색어
     * @param sort     정렬 기준 (new/read/like)
     * @param page     현재 페이지 번호
     * @return 게시글 리스트
     */
    @Override
    public List<StudyBoard> getBoardList(int studyNo, String key, String keyword, String sort, int page) {
        // 다른 서비스 로직이 있다면 여기서 추가 처리 가능
        return mapper.selectBoardList(studyNo, key, keyword, sort, page);
    }

    /**
     * 📄 게시글 상세 조회
     * @param boardNo 게시글 번호
     * @return 해당 게시글 객체
     */
    @Override
    public StudyBoard getBoardDetail(int boardNo) {
        return mapper.selectBoardDetail(boardNo);
    }
    
    /**
	 * 📋 스터디 ID로 스터디 정보 조회
	 * @param studyNo 스터디 번호
	 * @return 해당 스터디 객체
	 */
    @Override
    public Study selectStudyById(int studyNo) {
    	// TODO Auto-generated method stub
    	return null;
    }
}
