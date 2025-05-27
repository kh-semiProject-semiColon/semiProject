package kr.co.semi.studyboard.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.semi.member.model.dto.Member;
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
     * 📋 스터디 ID로 스터디 정보 조회
	 * @param studyNo 스터디 번호
	 * @return 해당 스터디 객체
     *
     */
    @Override
    public Study studyInfo(Member loginMember) {
    return mapper.studyInfo(loginMember);
    	
    }
}
