package kr.co.semi.main.Mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.semi.board.model.dto.Announce;
import kr.co.semi.board.model.dto.HireInfo;
import kr.co.semi.studyboard.model.dto.Study;

@Mapper
public interface MainMapper {

	/** 스터디 생성 by 김성원
	 * @param study
	 * @return
	 */
	int studyCreation(Study study);

	/** 스터디 번호 조회 by 김성원
	 * @param study
	 * @return
	 */
	int studyNo(Study study);

	/** 스터디-멤버 테이블에 적용과 동시에 리더 지정
	 * @param map
	 * @return
	 */
	int studyMember(Map<String, Object> map);

	/** 스터디명 중복검사
	 * @param studyName
	 * @return
	 */
	int studyNameConfirm(String studyName);

	/** 최신 구인게시글 조회
	 * @return
	 */
	HireInfo latedstPost();

	/** 최신 공지사항 글 조회
	 * @return
	 */
	Announce latestAnouncement(); 
}
