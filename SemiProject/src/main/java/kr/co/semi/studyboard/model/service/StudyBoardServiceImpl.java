package kr.co.semi.studyboard.model.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.co.semi.board.model.dto.Pagination;
import kr.co.semi.common.util.Utility;
import kr.co.semi.member.model.dto.Member;
import kr.co.semi.studyboard.model.dto.Study;
import kr.co.semi.studyboard.model.dto.StudyBoard;
import kr.co.semi.studyboard.model.mapper.StudyBoardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
@Slf4j
@PropertySource("classpath:/config.properties")

public class StudyBoardServiceImpl implements StudyBoardService {

	@Value("${my.studyProfile.folder-path}")
	private String folderPath;
	
	@Value("${my.studyProfile.web-path}")
	private String webPath;
	
    private final StudyBoardMapper mapper;

    // 스터디 정보 조회
    @Override
    public Study getStudyInfo(Member loginMember) {
        try {
            Study study = mapper.getStudyInfo(loginMember);
            System.out.println(study);
            
            if (study != null) {
                study.setCurrentMemberCount(mapper.getCurrentMemberCount(loginMember.getStudyNo()));
            }
            return study;
        } catch (Exception e) {
            log.error("스터디 정보 조회 중 오류 발생 - studyNo: {}, memberNo: {}", loginMember.getStudyNo(), loginMember.getMemberNo(), e);
            throw new RuntimeException("스터디 정보 조회 중 오류가 발생했습니다.", e);
        }
    }
    
    @Override
    public Map<String, Object> getStudyInfo(Member loginMember, int cp) {

    	// 1. 지정된 게시판(boardCode) 에서 삭제되지 않은 게시글 수를 조회
		// 삭제되지 않은 게시글 수를 조회
		int listCount = mapper.getStudyBoardListCount(loginMember.getStudyNo());
		
		
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
		List<StudyBoard> studyBoardList = mapper.selectStudyBoardList(loginMember.getStudyNo(), rowBounds);
		
		System.out.println(loginMember);
		// 4. 목록 조회 결과 + Pagination 객체를 Map으로 묵어서 반환
		Map<String, Object> map = new HashMap<>();
		
		map.put("pagination", pagination);
		map.put("studyBoardList", studyBoardList);
		
		// 5. 결과 반환
		
		return map;
    }

    // 스터디 정보 업데이트
    @Override 
    public int updateStudyInfo(Study study, MultipartFile imageFile) {
        try {
            
			String updatePath = null;
			String finalPath = null;
			
			// 변경명 저장
			String rename = null;
			
			// 업로드한 이미지가 있을 경우
			// - 있을 경우 : 경로 조합 (클라이언트 접근 경로 + 리네임파일명)
			if(!imageFile.isEmpty()) {
				
				// 1. 파일명 변경
				rename = Utility.fileRename(imageFile.getOriginalFilename());
				
				// 2. /myPage/profile/변경된 파일명
				updatePath = folderPath + rename;
				finalPath = webPath + rename;
				imageFile.transferTo(new File(updatePath));
				study.setStudyMainImg(finalPath);
			}
			
            int result = mapper.updateStudyInfo(study);
            log.info("스터디 정보 수정 - studyNo: {}, result: {}", study.getStudyNo(), result);
            return result ;
        } catch (Exception e) {
            log.error("스터디 정보 수정 중 오류 발생 - studyNo: {}", study.getStudyNo(), e);
            throw new RuntimeException("스터디 정보 수정 중 오류가 발생했습니다.", e);
        }
    }
    // 내규등록
    @Override
    public boolean updateRule(Study study) {
        try {
        	int result = 0;
        	
        	int ruleCount = mapper.ruleCount(study.getStudyNo());
        	
        	
        	
        	if(ruleCount >0) {
        		
        		 result = mapper.insertOrUpdateRule(study);
        	}else {
        		 result = mapper.insertRule(study);
        	}
        	
            return result > 0;
        } catch (Exception e) {
            log.error("스터디 내규 수정 중 오류 발생 - studyNo: {}", study.getStudyNo(), e);
            throw new RuntimeException("스터디 내규 수정 중 오류가 발생했습니다.", e);
        }
    }
    
    //스터디 팀장 위임
    @Override
    public boolean withdrawMember(Member loginMember) {
        try {
            if (isStudyLeader(loginMember.getMemberNo())) {
                throw new IllegalStateException("팀장은 직접 탈퇴할 수 없습니다. 팀장 권한을 위임하거나 스터디를 해체해주세요.");
            }

            int result = mapper.withdrawMember(loginMember);
            log.info("스터디 탈퇴 - studyNo: {}, memberNo: {}, result: {}", loginMember.getStudyNo(), loginMember.getMemberNo(), result);
            return result > 0;
        } catch (Exception e) {
            log.error("스터디 탈퇴 중 오류 발생 - studyNo: {}, memberNo: {}", loginMember.getStudyNo(), loginMember.getMemberNo(), e);
            if (e instanceof IllegalStateException) {
                throw e;
            }
            throw new RuntimeException("스터디 탈퇴 중 오류가 발생했습니다.", e);
        }
    }


    //팀장 권한인지 확인하는 메서드
    @Override
    public boolean isStudyLeader(int memberNo) {
        try {
            String role = mapper.checkMemberRole(memberNo);
            return "Y".equals(role);
        } catch (Exception e) {
            log.error("팀장 권한 확인 중 오류 발생 memberNo: {}", memberNo, e);
            return false;
        }
    }

    // 페이지 네이션
    @Override
    public Map<String, Object> getMyPosts(int studyNo, int memberNo, int page) {
        try {
            int limit = 10;
            int offset = (page - 1) * limit;
            
            List<StudyBoard> posts = mapper.getMyPosts(studyNo, memberNo, offset, limit);
            
            Map<String, Object> result = new HashMap<>();
            result.put("posts", posts);
            result.put("pagination", page);
            result.put("limit", limit);
            
            log.info("내 게시글 조회 - studyNo: {}, memberNo: {}, page: {}, count: {}", 
                    studyNo, memberNo, page, posts.size());
            
            return result;
        } catch (Exception e) {
            log.error("내 게시글 조회 중 오류 발생 - studyNo: {}, memberNo: {}, page: {}", 
                     studyNo, memberNo, page, e);
            throw new RuntimeException("게시글 조회 중 오류가 발생했습니다.", e);
        }
    }


    // 코멘트 
    @Override
    public int getCurrentMemberCount(int studyNo) {
        try {
            int count = mapper.getCurrentMemberCount(studyNo);
            log.debug("현재 멤버 수 조회 - studyNo: {}, count: {}", studyNo, count);
            return count;
        } catch (Exception e) {
            log.error("현재 멤버 수 조회 중 오류 발생 - studyNo: {}", studyNo, e);
            return 0;
        }
    }
    
    //스터디 멤버 목록 조회
    @Override
    public List<Member> getStudyMembers(int studyNo) {
        try {
            List<Member> members = mapper.getStudyMembers(studyNo);
            log.info("스터디 멤버 목록 조회 - studyNo: {}, count: {}", studyNo, members.size());
            return members;
        } catch (Exception e) {
            log.error("스터디 멤버 목록 조회 중 오류 발생 - studyNo: {}", studyNo, e);
            throw new RuntimeException("멤버 목록 조회 중 오류가 발생했습니다.", e);
        }
    }

    // 스터디 내규 작성
    @Override
	public String getStudyrule(Member loginMember) {
		
		return mapper.getStudyrule(loginMember);
	}
    
    
    // 검색 조건 있는 게시글
    @Override
    public Map<String, Object> searchList(Map<String, Object> paramMap, int page) {
		
    			
    			int listCount = mapper.getBoardSearchCount(paramMap);
    			
    			// 2. 1번의 결과 + cp를 이용해서
    			// Pagination 객체를 생성
    			Pagination pagination = new Pagination(page, listCount);
    			
    			// 3. 특정 게시판의 지정된 페이지 목록 조회
    			int limit = pagination.getLimit();
    			int offset = (page - 1) * limit;
    			RowBounds rowBounds = new RowBounds(offset,limit);
    			
    			// mapper 메서드 호출 코드 수행
    			// -> Mapper 메서드 호출 시 전달할 수 있는 매개변수 1개
    			// -> 2개를 전달할 수 있는 경우가 있음
    			// RowBounds를 이용할 때
    			// 1번째 : SQL에 전달할 파라미터
    			// 2번째 : RowBounds 객체
    			// 파라미터로 전달할 값 없을 경우 null이라도 전달
    			System.out.println(paramMap);
    			List<StudyBoard> studyBoardList = mapper.selectBoardSearchList(paramMap, rowBounds);
    			System.out.println(studyBoardList);
    			// 4. 목록 조회 결과 + Pagination 객체를 Map으로 묶음
    			Map<String, Object> map = new HashMap();
    			
    			map.put("pagination", pagination);
    			map.put("studyBoardList", studyBoardList);
    			
    			return map;
    }
    
    @Override
    public StudyBoard studyBoardOne(Map<String, Integer> map) {
    	return mapper.studyBoardOne(map);
    }
    
    @Override
    public int updateStudyBoardCount(int studyBoardNo) {
		// 1. 조회수 1 증가 (UPDATE)
		int result = mapper.updateStudyBoardCount(studyBoardNo);
		
		// 2. 현재 조회 수 조회
		if(result>0) {
			return mapper.selectStudyBoardCount(studyBoardNo);
		}
		
		// 실패한 경우 -1 반환
		return -1;
    }
    
    @Override
    public int studyBoardLike(Map<String, Integer> map) {
		int result = 0;
		
		// 1. 좋아요가 체크된 상태인 경우 (likeCheck == 1)
		// -> BOARD_LIKE 테이블에 DELETE 실행
		if(map.get("likeCheck") == 1) {
			
			result = mapper.deletestudyBoardLike(map);
			
		}else {
			// 2. 좋아요가 해제된 상태인 경우 (likeCheck == 0)
			// -> BOARD_LIKE 테이블에 INSERT 실행
			result = mapper.insertstudyBoardLike(map);
			
		}
		
		// 3. 다시 해당 게시글의 좋아요 개수를 조회해서 반환
		if(result > 0) {
			return mapper.selectLikeCount(map.get("studyBoardNo"));
		}
		
		
		return -1; // 좋아요 처리 실패
    }
    @Override
    @Transactional
    public boolean transferLeadershipAndWithdraw(Member member, Member loginMember) {
        try {
            // 1. 새로운 팀장으로 권한 변경
            int updateResult = mapper.updateMemberRole(member);
            if (updateResult <= 0) {
                return false;
            }
            // 2. 스터디 탈퇴 기능
            int deleteStudyMember = mapper.withdrawMemberById(loginMember);
            
            return true;
            
        } catch (Exception e) {
            throw new RuntimeException("팀장 권한 위임 처리 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    public boolean isStudyMember(Member member) {
        try {
        	
            int count = mapper.checkStudyMembership(member);
            return count > 0;
        } catch (Exception e) {
            return false;
        }
    }

	@Override
	public int studyDelete(Member loginMember) {
		int deleteStudyMember = mapper.withdrawMemberById(loginMember);
		int studyDelete = 0;
		if(deleteStudyMember > 0) {
			studyDelete = mapper.deleteStudy(loginMember);
		}
			
		return studyDelete;
	}

}