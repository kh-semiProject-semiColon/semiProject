package kr.co.semi.studyboard.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import kr.co.semi.studyboard.model.dto.StudyBoard;
import kr.co.semi.studyboard.model.service.StudyBoardService;
import lombok.RequiredArgsConstructor;

/**
 * 🎯 StudyBoardController
 * 📌 스터디 게시판 관련 요청을 처리하는 컨트롤러 클래스
 * 🔗 연동 구조: studyboardList.html, studyboardDetail.html 등과 연결됨
 */
@Controller
@RequestMapping("/studyBoard")
@RequiredArgsConstructor
public class StudyBoardController {

    private final StudyBoardService studyBoardService;
    
    @GetMapping("")
    public String studyBoard() {
    	return "studyboard/studyBoardList";
    }

    /**
     * 📄 게시판 목록 조회
     * - 검색어, 정렬조건, 페이지 정보를 받아 게시글 목록 출력
     * - 기본 정렬은 최신순(new), 키워드/검색기능/정렬 기능 혼합됨
     *
     * @param studyNo 현재 선택된 스터디 번호
     * @param page 페이지 번호 (기본값 1)
     * @param sort 정렬 기준 (new/read/like)
     * @param key 검색 키 (제목, 작성자 등)
     * @param keyword 검색어
     */
    @GetMapping("/list/{studyNo}")
    public String boardList(@PathVariable int studyNo,
                            @RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "new") String sort,
                            @RequestParam(required = false) String key,
                            @RequestParam(required = false) String keyword,
                            Model model,
                            HttpSession session) {

//        int memberNo = (int) session.getAttribute("loginMemberNo");
//
//        // 📌 게시글 목록 + 페이징 처리
//        List<StudyBoard> boardList = studyBoardService.getBoardList(studyNo, key, keyword, sort, page);
//
//        // 모델에 데이터 추가 (뷰에서 사용될 변수들)
//        model.addAttribute("boardList", boardList);
//        model.addAttribute("selectedStudyNo", studyNo);
//        model.addAttribute("key", key);
//        model.addAttribute("keyword", keyword);
//        model.addAttribute("sort", sort);
//        model.addAttribute("page", page);

        return "studyboard/studyboardList";
    }

    /**
     * 📄 게시글 상세보기
     * - 해당 게시글 번호를 받아 상세 페이지로 이동
     *
     * @param boardNo 게시글 번호
     */
    @GetMapping("/detail/{boardNo}")
    public String boardDetail(@PathVariable int boardNo,
                               Model model) {
        StudyBoard board = studyBoardService.getBoardDetail(boardNo);

        model.addAttribute("board", board);

        return "studyboard/studyboardDetail";
    }
}
