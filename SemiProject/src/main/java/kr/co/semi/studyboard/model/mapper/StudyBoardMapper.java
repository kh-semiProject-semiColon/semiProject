package kr.co.semi.studyboard.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.semi.studyboard.model.dto.StudyBoard;

/**
 * 🎯 StudyBoardMapper
 * 📌 MyBatis 매퍼 인터페이스
 * 🔗 XML 파일과 연동되어 실제 SQL 실행
 */
@Mapper
public interface StudyBoardMapper {

    /**
     * 📋 게시글 목록 조회
     */
    List<StudyBoard> selectBoardList(
        @Param("studyNo") int studyNo,
        @Param("key") String key,
        @Param("keyword") String keyword,
        @Param("sort") String sort,
        @Param("page") int page
    );

    /**
     * 📄 게시글 상세 조회
     */
    StudyBoard selectBoardDetail(@Param("boardNo") int boardNo);
}
