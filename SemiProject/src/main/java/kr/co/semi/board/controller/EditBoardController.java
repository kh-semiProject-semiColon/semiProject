package kr.co.semi.board.controller;



import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.semi.board.model.dto.Board;
import kr.co.semi.board.model.service.BoardService;
import kr.co.semi.board.model.service.EditBoardService;
import kr.co.semi.member.model.dto.Member;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("editBoard")
@RequiredArgsConstructor
@PropertySource("classpath:/config.properties")
public class EditBoardController {

	private final EditBoardService service;
	private final BoardService bService;
	
	@Value("${my.board.web-path}")
	private String webPath;
	
	@Value("${my.board.folder-path}")
	private String folderPath;
	
	@GetMapping("{boardCode:[0-9]+}/insert")
	public String boardInsert(@PathVariable("boardCode") int boardCode) {
		
		return "board/boardWrite";
	}
	
	
	/**
	 * @param boardCode		: 어떤 게시판에 작성될 글인지 구분
	 * @param inputBoard	: 입력된 값중 제목과 내용 세팅
	 * @param loginMember	: 로그인한 회원 정보
	 * @param images		: 제출된 file 타입 input 태그가 전달하는 데이터
	 * @param ra
	 * @return
	 */
	@PostMapping("{boardCode:[0-9]+}/insert")
	public String boardInsert(@PathVariable("boardCode") int boardCode,
							  @ModelAttribute Board inputBoard,
							  @SessionAttribute("loginMember") Member loginMember,
							  RedirectAttributes ra) throws Exception {
		
		
	    String cleanedContent = Jsoup.parse(inputBoard.getBoardContent()).body().html();
	    inputBoard.setBoardContent(cleanedContent);
			
		
		// 1. boardCode, 로그인한 회원번호(memberNo)를 inputBoard에 세팅
		inputBoard.setBoardCode(boardCode);
		inputBoard.setMemberNo(loginMember.getMemberNo());
		// -> inputBoard에는 총 4가지 값이 세팅됨 => boardTitle, boardContent, boardCode, memberNo

		// 2. 서비스 메서드 호출 후 결과 반환
		// -> 성공 시 [상세조회]를 요청할 수 있도록
		//    삽입된 게시글의 번호를 반환받기
		int boardNo = service.boardInsert(inputBoard);
		
		// 3. 서비스 결과에 따라 message, 리다이렉트 경로 지정
		String path = null;
				
		String message = null;
		
		if(boardNo > 0) {
			message = "게시글이 작성되었습니다";
			path = "/board/"+boardCode+"/"+boardNo;
		}else {
			
			path = "insert";
			message = "게시글 작성 실패";
		}
		
		ra.addFlashAttribute("message", message);
		
		
		return "redirect:"+path;
	}

	
}
