package kr.co.semi.studyboard.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.semi.member.model.dto.Member;
import kr.co.semi.studyboard.model.dto.Study;
import kr.co.semi.studyboard.model.dto.StudyBoard;

/**
 * 🎯 StudyBoardMapper
 * 📌 MyBatis 매퍼 인터페이스
 * 🔗 XML 파일과 연동되어 실제 SQL 실행
 */
@Mapper
public interface StudyBoardMapper {


	Study studyInfo(Member loginMember);
}
