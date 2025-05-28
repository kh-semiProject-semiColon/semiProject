package kr.co.semi.studyboard.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.semi.member.model.dto.Member;
import kr.co.semi.studyboard.model.dto.Study;
import kr.co.semi.studyboard.model.service.StudyBoardService;
import lombok.extern.slf4j.Slf4j;

@Controller
@SessionAttributes("loginMember")
@RequestMapping("studyBoard")
@Slf4j
public class StudyBoardController {

    @Autowired
    private StudyBoardService service;
    
    
    
    // ============================================
    // 스터디 스케줄 관련
    // ============================================
    
    /**
     * 스터디 스케줄 페이지
     */
    @GetMapping("calendar")
    public String studyBoardCalendar() {
        return "studyBoard/calendar";
    }
    
  

    // ============================================
    // 스터디 게시판 관련
    // ============================================
    
    /**
     * 스터디 게시판 페이지 (내 게시글, 내 댓글)
     */
    @GetMapping("studyboard")
    public String studyBoard(@SessionAttribute("loginMember") Member loginMember,
                           @RequestParam(value = "cp",required = false, defaultValue = "1" ) int page,
                           Model model,
                           RedirectAttributes ra) {
        try {
        	String message = null;
        	
            Study study = service.getStudyInfo(loginMember);
            
            if (study == null) {
                message = "스터디 없습니다";
                return "redirect:/study/studyNow";
            }
            
            Map<String, Object> postData = service.getMyPosts(loginMember.getStudyNo(), loginMember.getMemberNo(), page);
            
            model.addAttribute("study", study);
            model.addAttribute("posts", postData.get("posts"));
            model.addAttribute("currentPage", page);
            model.addAttribute("studyNo", loginMember.getStudyNo());
            
            log.info("스터디 게시판 페이지 접근 - studyNo: {}, memberNo: {}, page: {}", 
                    loginMember.getStudyNo(), loginMember.getMemberNo(), page);
            
        } catch (Exception e) {
            log.error("스터디 게시판 페이지 오류", e);
            model.addAttribute("errorMessage", "게시판 정보를 불러오는 중 오류가 발생했습니다.");
        }
        
        return "studyBoard/studyboard";
    }
    
    // ============================================
    // 스터디 정보 수정 관련
    // ============================================
    
    /**
     * 스터디 정보 수정 페이지
     */
    @GetMapping("update")
    public String studyBoardUpdate(@SessionAttribute("loginMember") Member loginMember,
                                 Model model) {
        try {
            Study study = service.getStudyInfo(loginMember);
            if (study == null) {
                log.warn("존재하지 않는 스터디 또는 권한 없음 - studyNo: {}, memberNo: {}", 
                		loginMember.getStudyNo(), loginMember.getMemberNo());
                return "redirect:/study/studyNow";
            }
            
            model.addAttribute("study", study);
            
            log.info("스터디 정보 수정 페이지 접근 - studyNo: {}, memberNo: {}, isLeader: {}", 
            		loginMember.getStudyNo(), loginMember.getMemberNo(), study.isLeader());
            
        } catch (Exception e) {
            log.error("스터디 정보 수정 페이지 오류", e);
            model.addAttribute("errorMessage", "스터디 정보를 불러오는 중 오류가 발생했습니다.");
        }
        
        return "studyBoard/update";
    }

    /**
     * 스터디 정보 수정 처리 - DTO로 받기 (완전 수정 버전)
     */
    @PostMapping("update")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateStudy(@SessionAttribute("loginMember") Member loginMember,
                                                          Study study,  // DTO로 받기
                                                          @RequestBody(required = false) MultipartFile imageFile) {
        
        Map<String, Object> response = new HashMap<>();
        try {
            // 팀장 권한 확인
            if (!service.isStudyLeader(loginMember.getMemberNo())) {
                response.put("success", false);
                response.put("message", "팀장만 스터디 정보를 수정할 수 있습니다.");
                return ResponseEntity.ok(response); // 200
            }
            
            // 현재 멤버 수보다 최대 인원이 적으면 안됨
            int currentMemberCount = service.getCurrentMemberCount(study.getStudyNo());
            if (study.getStudyMaxCount() < currentMemberCount) {
                response.put("success", false);
                response.put("message", String.format("현재 참여 인원(%d명)보다 적게 설정할 수 없습니다.", currentMemberCount));
                return ResponseEntity.ok(response);
            }
            
            
            // DTO로 받은 데이터를 바로 서비스로 전달
            int result = service.updateStudyInfo(study, imageFile);
            
            response.put("success", result);
            response.put("message", result == 1 ? "스터디 정보가 수정되었습니다." : "수정에 실패했습니다.");
            
            if (result>0) {
                log.info("스터디 정보 수정 성공 - studyNo: {}, memberNo: {}, studyName: {}", 
                        study.getStudyNo(), loginMember.getMemberNo(), study.getStudyName());
            }else {
            	log.info("수정실패");
            }
            
        } catch (Exception e) {
            log.error("스터디 정보 수정 중 오류 발생 - studyNo: {}", study.getStudyNo(), e);
            response.put("success", false);
            response.put("message", "오류가 발생했습니다: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }

    // ============================================
    // 스터디 탈퇴 관련
    // ============================================
    
    /**
     * 스터디 탈퇴 페이지
     */
    @GetMapping("delete")
    public String studyBoardDelete(@SessionAttribute("loginMember") Member loginMember,
                                 @RequestParam("studyNo") int studyNo, Model model) {
        try {
            Study study = service.getStudyInfo(loginMember);
            if (study == null) {
                log.warn("존재하지 않는 스터디 또는 권한 없음 - studyNo: {}, memberNo: {}", 
                        studyNo, loginMember.getMemberNo());
                return "redirect:/study/studyNow";
            }
            
            model.addAttribute("study", study);
            model.addAttribute("studyNo", studyNo);
            
            log.info("스터디 탈퇴 페이지 접근 - studyNo: {}, memberNo: {}, isLeader: {}", 
                    studyNo, loginMember.getMemberNo(), study.isLeader());
            
        } catch (Exception e) {
            log.error("스터디 탈퇴 페이지 오류", e);
            model.addAttribute("errorMessage", "스터디 정보를 불러오는 중 오류가 발생했습니다.");
        }
        
        return "studyBoard/delete";
    }

    /**
     * 스터디 탈퇴 처리 - DTO로 받기
     */
    @PostMapping("delete")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> withdrawFromStudy(@SessionAttribute("loginMember") Member loginMember,
                                                               Study study) {  // DTO로 받기
        Map<String, Object> response = new HashMap<>();
        try {
            boolean result = service.withdrawMember(study.getStudyNo(), loginMember.getMemberNo());
            
            response.put("success", result);
            response.put("message", result ? "스터디에서 탈퇴되었습니다." : "탈퇴에 실패했습니다.");
            
            if (result) {
                log.info("스터디 탈퇴 성공 - studyNo: {}, memberNo: {}", study.getStudyNo(), loginMember.getMemberNo());
                response.put("redirectUrl", "/study/studyNow");
            }
            
        } catch (IllegalStateException e) {
            log.warn("스터디 탈퇴 제한 - studyNo: {}, memberNo: {}, reason: {}", 
                    study.getStudyNo(), loginMember.getMemberNo(), e.getMessage());
            response.put("success", false);
            response.put("message", e.getMessage());
        } catch (Exception e) {
            log.error("스터디 탈퇴 중 오류 발생", e);
            response.put("success", false);
            response.put("message", "오류가 발생했습니다: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
    
    // ============================================
    // 스터디 해체 관련
    // ============================================
    
    /**
     * 스터디 해체 페이지
     */
    @GetMapping("delete1")
    public String studyBoardDelete1(@SessionAttribute("loginMember") Member loginMember,
                                  @RequestParam("studyNo") int studyNo, Model model) {
        try {
            Study study = service.getStudyInfo(loginMember);
            if (study == null) {
                log.warn("존재하지 않는 스터디 또는 권한 없음 - studyNo: {}, memberNo: {}", 
                        studyNo, loginMember.getMemberNo());
                return "redirect:/study/studyNow";
            }
            
            // 팀장만 접근 가능
            if (!service.isStudyLeader(loginMember.getMemberNo())) {
                log.warn("팀장 권한 없음 - studyNo: {}, memberNo: {}", studyNo, loginMember.getMemberNo());
                return "redirect:/studyBoard/delete?studyNo=" + studyNo;
            }
            
            model.addAttribute("study", study);
            model.addAttribute("studyNo", studyNo);
            
            log.info("스터디 해체 페이지 접근 - studyNo: {}, memberNo: {}", 
                    studyNo, loginMember.getMemberNo());
            
        } catch (Exception e) {
            log.error("스터디 해체 페이지 오류", e);
            model.addAttribute("errorMessage", "스터디 정보를 불러오는 중 오류가 발생했습니다.");
        }
        
        return "studyBoard/delete1";
    }

    /**
     * 스터디 해체 처리 - DTO로 받기
     */
    @PostMapping("delete1")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteStudy(@SessionAttribute("loginMember") Member loginMember,
                                                          Study study) {  // DTO로 받기
        Map<String, Object> response = new HashMap<>();
        try {
            // 팀장 권한 확인
            if (!service.isStudyLeader(loginMember.getMemberNo())) {
                response.put("success", false);
                response.put("message", "팀장만 스터디를 해체할 수 있습니다.");
                return ResponseEntity.ok(response);
            }
            
            boolean result = service.deleteStudy(study.getStudyNo());
            
            response.put("success", result);
            response.put("message", result ? "스터디가 해체되었습니다." : "해체에 실패했습니다.");
            
            if (result) {
                log.info("스터디 해체 성공 - studyNo: {}, memberNo: {}", study.getStudyNo(), loginMember.getMemberNo());
                response.put("redirectUrl", "/study/studyNow");
            }
            
        } catch (Exception e) {
            log.error("스터디 해체 중 오류 발생", e);
            response.put("success", false);
            response.put("message", "오류가 발생했습니다: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
    
    // ============================================
    // 스터디 내규 관련
    // ============================================
    
    /**
     * 스터디 내규 페이지
     */
    @GetMapping("rulecontent")
    public String studyBoardRuleContent(@SessionAttribute("loginMember") Member loginMember,
                                       Model model) {
        try {
            Study study = service.getStudyInfo(loginMember);
            study.setRuleContent(service.getStudyrule(loginMember));
            if (study == null) {
                log.warn("존재하지 않는 스터디 또는 권한 없음 - studyNo: {}, memberNo: {}", 
                        loginMember.getStudyNo(), loginMember.getMemberNo());
                return "redirect:/study/studyNow";
            }
            
            model.addAttribute("study", study);
            model.addAttribute("studyNo",  loginMember.getStudyNo());
            
            log.info("스터디 내규 페이지 접근 - studyNo: {}, memberNo: {}, isLeader: {}", 
            		 loginMember.getStudyNo(), loginMember.getMemberNo(), study.isLeader());
            
        } catch (Exception e) {
            log.error("스터디 내규 페이지 오류", e);
            model.addAttribute("errorMessage", "스터디 정보를 불러오는 중 오류가 발생했습니다.");
        }
        
        return "studyBoard/rulecontent";
    }

    /**
     * 스터디 내규 등록/수정 처리 - DTO로 받기
     */
    @PostMapping("rulecontent")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateRule(@SessionAttribute("loginMember") Member loginMember,
                                                         Study study) {  // DTO로 받기
        Map<String, Object> response = new HashMap<>();
        try {
            // 팀장 권한 확인
            if (!service.isStudyLeader( loginMember.getMemberNo())) {
                response.put("success", false);
                response.put("message", "팀장만 내규를 수정할 수 있습니다.");
                return ResponseEntity.ok(response);
            }
            
            // 내규 내용 검증
            if (study.getRuleContent() == null || study.getRuleContent().trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "내규 내용을 입력해주세요.");
                return ResponseEntity.ok(response);
            }
            
            if (study.getRuleContent().length() > 4000) {
                response.put("success", false);
                response.put("message", "내규 내용은 4000자를 초과할 수 없습니다.");
                return ResponseEntity.ok(response);
            }
            
            boolean result = service.updateRule(study.getStudyNo(), study.getRuleContent().trim());
            
            response.put("success", result);
            response.put("message", result ? "내규가 저장되었습니다." : "저장에 실패했습니다.");
            
            if (result) {
                log.info("스터디 내규 수정 성공 - studyNo: {}, memberNo: {}", study.getStudyNo(), loginMember.getMemberNo());
            }
            
        } catch (Exception e) {
            log.error("스터디 내규 수정 중 오류 발생", e);
            response.put("success", false);
            response.put("message", "오류가 발생했습니다: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
    
    // ============================================
    // 유틸리티 메서드들
    // ============================================
    
    /**
     * 스터디 멤버 목록 조회 (AJAX)
     */
    @GetMapping("members")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getStudyMembers(@SessionAttribute("loginMember") Member loginMember,
                                                              @RequestParam int studyNo) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 스터디 멤버인지 확인
            Study study = service.getStudyInfo(loginMember);
            if (study == null) {
                response.put("success", false);
                response.put("message", "스터디 멤버가 아닙니다.");
                return ResponseEntity.ok(response);
            }
            
            List<Map<String, Object>> members = service.getStudyMembers(studyNo);
            
            response.put("success", true);
            response.put("members", members);
            response.put("totalCount", members.size());
            
        } catch (Exception e) {
            log.error("스터디 멤버 목록 조회 중 오류 발생", e);
            response.put("success", false);
            response.put("message", "오류가 발생했습니다: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
}