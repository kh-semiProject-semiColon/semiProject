package kr.co.semi.studyboard.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.co.semi.member.model.dto.Member;
import kr.co.semi.studyboard.model.dto.Study;
import kr.co.semi.studyboard.model.dto.StudyBoard;
import kr.co.semi.studyboard.model.dto.StudyComment;
import kr.co.semi.studyboard.model.mapper.StudyBoardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class StudyBoardServiceImpl implements StudyBoardService {

    private final StudyBoardMapper mapper;

    @Override
    public Study getStudyInfo(Member loginMember) {
        try {
            Study study = mapper.getStudyInfo(loginMember);
            
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
    public int updateStudyInfo(Study study, MultipartFile imageFile) {
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                log.info("이미지 파일 업로드: {}", imageFile.getOriginalFilename());
                // 이미지 파일 업로드 로직 추가 필요
            }
            
            int result = mapper.updateStudyInfo(study);
            log.info("스터디 정보 수정 - studyNo: {}, result: {}", study.getStudyNo(), result);
            return result ;
        } catch (Exception e) {
            log.error("스터디 정보 수정 중 오류 발생 - studyNo: {}", study.getStudyNo(), e);
            throw new RuntimeException("스터디 정보 수정 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    public boolean updateRule(int studyNo, String ruleContent) {
        try {
            int result = mapper.insertOrUpdateRule(studyNo, ruleContent);
            log.info("스터디 내규 수정 - studyNo: {}, result: {}", studyNo, result);
            return result > 0;
        } catch (Exception e) {
            log.error("스터디 내규 수정 중 오류 발생 - studyNo: {}", studyNo, e);
            throw new RuntimeException("스터디 내규 수정 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    public boolean withdrawMember(int studyNo, int memberNo) {
        try {
            if (isStudyLeader(memberNo)) {
                throw new IllegalStateException("팀장은 직접 탈퇴할 수 없습니다. 팀장 권한을 위임하거나 스터디를 해체해주세요.");
            }
            
            int result = mapper.withdrawMember(studyNo, memberNo);
            log.info("스터디 탈퇴 - studyNo: {}, memberNo: {}, result: {}", studyNo, memberNo, result);
            return result > 0;
        } catch (Exception e) {
            log.error("스터디 탈퇴 중 오류 발생 - studyNo: {}, memberNo: {}", studyNo, memberNo, e);
            if (e instanceof IllegalStateException) {
                throw e;
            }
            throw new RuntimeException("스터디 탈퇴 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    public boolean deleteStudy(int studyNo) {
        try {
            int result = mapper.deleteStudy(studyNo);
            log.info("스터디 해체 - studyNo: {}, result: {}", studyNo, result);
            return result > 0;
        } catch (Exception e) {
            log.error("스터디 해체 중 오류 발생 - studyNo: {}", studyNo, e);
            throw new RuntimeException("스터디 해체 중 오류가 발생했습니다.", e);
        }
    }

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

    @Override
    public Map<String, Object> getMyPosts(int studyNo, int memberNo, int page) {
        try {
            int limit = 10;
            int offset = (page - 1) * limit;
            
            List<StudyBoard> posts = mapper.getMyPosts(studyNo, memberNo, offset, limit);
            
            Map<String, Object> result = new HashMap<>();
            result.put("posts", posts);
            result.put("currentPage", page);
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

    @Override
    public Map<String, Object> getMyComments(int studyNo, int memberNo, int page) {
        try {
            int limit = 10;
            int offset = (page - 1) * limit;
            
            List<StudyComment> comments = mapper.getMyComments(studyNo, memberNo, offset, limit);
            
            Map<String, Object> result = new HashMap<>();
            result.put("comments", comments);
            result.put("currentPage", page);
            result.put("limit", limit);
            
            log.info("내 댓글 조회 - studyNo: {}, memberNo: {}, page: {}, count: {}", 
                    studyNo, memberNo, page, comments.size());
            
            return result;
        } catch (Exception e) {
            log.error("내 댓글 조회 중 오류 발생 - studyNo: {}, memberNo: {}, page: {}", 
                     studyNo, memberNo, page, e);
            throw new RuntimeException("댓글 조회 중 오류가 발생했습니다.", e);
        }
    }

 


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
    
    @Override
    public List<Map<String, Object>> getStudyMembers(int studyNo) {
        try {
            List<Map<String, Object>> members = mapper.getStudyMembers(studyNo);
            log.info("스터디 멤버 목록 조회 - studyNo: {}, count: {}", studyNo, members.size());
            return members;
        } catch (Exception e) {
            log.error("스터디 멤버 목록 조회 중 오류 발생 - studyNo: {}", studyNo, e);
            throw new RuntimeException("멤버 목록 조회 중 오류가 발생했습니다.", e);
        }
    }

    @Override
	public String getStudyrule(Member loginMember) {
		
		return mapper.getStudyrule(loginMember);
	}
}