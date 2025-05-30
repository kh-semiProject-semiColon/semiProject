package kr.co.semi.studyboard.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import kr.co.semi.member.model.dto.Member;
import kr.co.semi.studyboard.model.dto.Study;
import kr.co.semi.studyboard.model.dto.StudyBoard;
import kr.co.semi.studyboard.model.dto.StudyComment;

@Mapper
public interface StudyBoardMapper {

   
    
    // 스터디 정보 수정
    int updateStudyInfo(Study study);
    
    // 팀장권한 조회
    String checkMemberRole(int memberNo);
    
    // 스터디 내규 조회
    int ruleCount(int studyNo);

    // 스터디 내규 수정
    int insertOrUpdateRule(Study study);
    
    // 스터디 내규 등록
	int insertRule(Study study);

    
    
    // 스터디 멤버 탈퇴
    int withdrawMember(Member loginMember);
    
    
    // 스터디 해체
    int deleteStudy(@Param("studyNo") int studyNo);
    

    // 내 게시글 조회
    List<StudyBoard> getMyPosts(@Param("studyNo") int studyNo, @Param("memberNo") int memberNo, 
                               @Param("offset") int offset, @Param("limit") int limit);
    
        
    // 현재 멤버 수 조회
    int getCurrentMemberCount(@Param("studyNo") int studyNo);
    
    // 스터디 멤버 목록 조회
    List<Map<String, Object>> getStudyMembers(@Param("studyNo") int studyNo);

    // 내규 조회 서비스
    String getStudyrule(Member loginMember);


	/** 검색 조건에 맞는 게시글 개수
	 * @param paramMap
	 * @return
	 */
	int getBoardSearchCount(Map<String, Object> paramMap);


	/** 검색 조건에 맞는 게시글 정보
	 * @param paramMap
	 * @param rowBounds
	 * @return
	 */
	List<StudyBoard> selectBoardSearchList(Map<String, Object> paramMap, RowBounds rowBounds);

	/** 게시글 개수
	 * @param studyNo
	 * @return
	 */
	int getStudyBoardListCount(int studyNo);

	/** 게시글 개수
	 * @param studyNo
	 * @param rowBounds
	 * @return
	 */
	List<StudyBoard> selectStudyBoardList(int studyNo, RowBounds rowBounds);

	Study getStudyInfo(Member loginMember);

	StudyBoard studyBoardOne(Map<String, Integer> map);

	int updateStudyBoardCount(int studyBoardNo);

	int selectStudyBoardCount(int studyBoardNo);

	int deletestudyBoardLike(Map<String, Integer> map);

	int insertstudyBoardLike(Map<String, Integer> map);

	int selectLikeCount(Integer integer);









}