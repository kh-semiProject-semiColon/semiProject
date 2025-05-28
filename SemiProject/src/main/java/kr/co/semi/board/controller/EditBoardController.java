package kr.co.semi.board.controller;



import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.semi.board.model.dto.Board;
import kr.co.semi.board.model.service.BoardService;
import kr.co.semi.board.model.service.EditBoardService;
import kr.co.semi.common.util.Utility;
import kr.co.semi.member.model.dto.Member;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("editBoard")
@RequiredArgsConstructor
@Configuration
@PropertySource("classpath:/config.properties")
public class EditBoardController {

	private final EditBoardService service;
	private final BoardService bService;
	
	@Value("${my.board.temp.folder-path}")
	private String tempFolderPath;
	
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

	   @ResponseBody
	   @PostMapping("{boardCode:[0-9]+}/uploadImg")
	   public String uploadSummernoteImage(@RequestParam("image") MultipartFile image,
	                                       HttpServletRequest req) throws IOException {

	       String imgPath = folderPath; 

	       String imgOriginalName = image.getOriginalFilename();
	       String imgRename = Utility.fileRename(imgOriginalName);

	       File target = new File(imgPath + imgRename);
	       image.transferTo(target);
	       
	       String imageUrl = req.getContextPath()+ webPath + imgRename;
	       return imageUrl;
	   }
	   
		/** 게시글 수정 화면 전환
		 * @param boardCode     : 게시판 종류 번호
		 * @param boardNumber	: 게시글 번호
		 * @param loginMember	: 현재 로그인한 회원 객체(로그인한 회원이 작성한 글이 맞는지 검사)
		 * @param model		
		 * @param ra
		 * @return
		 */
		@GetMapping("{boardCode:[0-9]+}/{boardNo:[0-9]+}/update")
		public String boardUpdate(@PathVariable("boardCode") int boardCode,
								  @PathVariable("boardNo") int boardNo,
								  @SessionAttribute("loginMember") Member loginMember,
								  Model model,
								  RedirectAttributes ra){
			
			// 수정 화면에 출력할 기존의 제목/내용/이미지 조회
			// -> 게시글 상세 조회
			Map<String, Integer> map = new HashMap();
			map.put("boardCode", boardCode);
			map.put("boardNo", boardNo);
			
			// BoardService.selectOne(map)호출
			Board board = bService.selectOne(map);
			
			String message = null;
			String path = null;
			
			if(board == null) {
				message = "해당 게시글이 존재하지 않습니다.";
				path = "redirect:/board"+boardCode;
				
				ra.addFlashAttribute(message);
			} else if(board.getMemberNo() != loginMember.getMemberNo()) {
				message = "자신이 작성한 글만 수정 가능합니다!";
				
				// 해당 글 상세조회 리다이렉트
				path = String.format("redirect:/board/%d/%d", boardCode, boardNo);
				
				ra.addFlashAttribute(message);
			} else {
				
				path = "board/boardUpdate"; // templates/board/boardUpdate.html로 forward
				model.addAttribute("board", board);
				
			}
			return path;
		}
	   
	   
	   
	   @PostMapping("{boardCode:[0-9]+}/{boardNo:[0-9]+}/update")
		public String boardUpdate(@PathVariable("boardCode") int boardCode,
								  @PathVariable("boardNo") int boardNo,
								  Board inputBoard,
								  @SessionAttribute("loginMember") Member loginMember,
								  RedirectAttributes ra,
								  @RequestParam(value = "cp", required = false, defaultValue = "1") int cp) throws Exception {
			
			// 1. 커맨드 객체 inputBoard에 boardCode, boardNo, memberNo 세팅
			inputBoard.setBoardCode(boardCode);
			inputBoard.setBoardNo(boardNo);
			inputBoard.setMemberNo(loginMember.getMemberNo());
			// inputBoard -> 제목, 내용, boardCode, boardNo, memberNo
			
			// 2. 게시글 수정 서비스 호출 후 결과 반환 받기
			int result = service.boardUpdate(inputBoard);
			
			// 3. 서비스 결과에 따라 응답 제어
			String message = null;
			String path = null;
			
			if(result > 0) {
				
				message = "게시글이 수정 되었습니다";
				path = String.format("board/%d/%d?cp=%d",boardCode, boardNo, cp);
				
			}else {
				
				message = "수정이 실패하였습니다";
				path = "update";
				
			}
			
			ra.addFlashAttribute("message", message);
			
			return "redirect:/" + path;
		}
		
		// /editBoard/1/2004/delete?cp=1
		@PostMapping("{boardCode:[0-9]+}/{boardNo:[0-9]+}/delete")
		public String boardDelete(@PathVariable("boardCode") int boardCode,
								  @PathVariable("boardNo") int boardNo,
								  @RequestParam(value = "cp", required = false, defaultValue = "1") int cp,
								  RedirectAttributes ra,
								  @SessionAttribute("loginMember") Member loginMember) {
			
			
			Map<String, Integer> map = new HashMap();
			map.put("boardCode", boardCode);
			map.put("boardNo", boardNo);
			map.put("memberNo", loginMember.getMemberNo());
			
			int result = service.boardDelete(map);
			
			String message = null;
			String path = null;
					
			if(result > 0) {
				
				message = "삭제되었습니다.";
				path = String.format("/board/%d?cp=%d", boardCode, cp);
			
			}else {
				
				message = "삭제에 실패하였습니다";
				path = String.format("/board/%d/%d?cp=%d", boardCode, boardNo, cp);

			}
			
			ra.addFlashAttribute("message", message);
			
			return "redirect:" + path;
		}

}
