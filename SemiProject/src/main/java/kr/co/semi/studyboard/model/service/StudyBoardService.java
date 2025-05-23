package kr.co.semi.studyboard.model.service;

import kr.co.semi.studyboard.model.dto.StudyBoard;

import java.util.List;

/**
 * 🎯 StudyBoardService
 * 📌 게시판 기능에 대한 인터페이스 정의
 * 🔗 Controller에서 호출되며, 구현체는 StudyBoardServiceImpl
 */
public interface StudyBoardService {

    /**
     * 📋 게시글 목록 조회
     *
     * @param studyNo  스터디 번호
     * @param key      검색 키 (제목/작성자 등)
     * @param keyword  검색어
     * @param sort     정렬 기준
     * @param page     현재 페이지
     * @return 조건에 맞는 게시글 리스트
     */
    List<StudyBoard> getBoardList(int studyNo, String key, String keyword, String sort, int page);

    /**
     * 📄 게시글 상세 조회
     * @param boardNo 게시글 번호
     * @return 게시글 상세 객체
     */
    StudyBoard getBoardDetail(int boardNo);
}
