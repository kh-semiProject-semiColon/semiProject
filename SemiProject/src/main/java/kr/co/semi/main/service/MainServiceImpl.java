package kr.co.semi.main.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.semi.board.model.dto.Announce;
import kr.co.semi.board.model.dto.HireInfo;
import kr.co.semi.main.Mapper.MainMapper;
import kr.co.semi.studyboard.model.dto.Study;

@Service
@Transactional(rollbackFor = Exception.class)
public class MainServiceImpl implements MainService {
	
	@Autowired
	private MainMapper mapper;

	/** 스터디 생성 서비스
	 * by 김성원
	 */
	@Override
	public int studyCreation(Study study, int memberNo) {
		// 스터디 생성
		int result = mapper.studyCreation(study);
		
		// 스터디 생성 후 스터디-멤버 테이블에 추가

		// 생성된 스터디에 부여된 번호를 가져옴
		int studyNo = mapper.studyNo(study);
		
		// 첫 스터디 생성하면 바로 스터디의 리더가 됨
		Map<String, Object> map = new HashMap<>();
		map.put("memberNo", memberNo);
		map.put("studyNo", studyNo);
		int studyMember = mapper.studyMember(map);

		if(studyMember == 0) {
			result = 0;
		}
		
		return studyNo;
	}

	/**
	 * 스터디명 중복 검사
	 */
	@Override
	public int studyNameConfirm(String studyName) {
		int studyNameConfirm = mapper.studyNameConfirm(studyName);
		return studyNameConfirm;
	}

	/**
	 * 최신 구인게시글 조회
	 */
	@Override
	public HireInfo latedstPost() {
		return mapper.latedstPost();
	}

	/**
	 * 최신 공지사항 글 조회
	 */
	@Override
	public Announce latestAnouncement() {
		return mapper.latestAnouncement();
	}
	
	

}
