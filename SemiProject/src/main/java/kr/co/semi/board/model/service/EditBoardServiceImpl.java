package kr.co.semi.board.model.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.co.semi.board.model.dto.Board;
import kr.co.semi.board.model.dto.BoardImg;
import kr.co.semi.board.model.mapper.EditBoardMapper;
import kr.co.semi.common.util.Utility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Transactional(rollbackFor = Exception.class)
@Service
@RequiredArgsConstructor
@Slf4j
@PropertySource("classpath:/config.properties")
public class EditBoardServiceImpl implements EditBoardService{

	private final EditBoardMapper mapper;
	
	@Value("${my.board.temp.folder-path}")
	private String tempFolderPath;
	
	@Value("${my.board.web-path}")
	private String webPath;
	
	@Value("${my.board.folder-path}")
	private String folderPath;
	
	@Override
		public int boardInsert(Board inputBoard) {
		
				// 1. 게시글 부분을 먼저 BOARD 테이플에 INSERT하기
				// -> INSERT 결과로 작성된 게시글 번호 반환
				int result = mapper.boardInsert(inputBoard);
				
				// result == INSERT 결과 (삽입 성공한 행의 개수 0 or 1)
				
				// 삽입 실패 시
				if(result == 0) {
					return 0;
				}
				
				// 삽입 성공 시
				// 삽입된 게시글의 번호를 변수로 저장
				// mapper.xml에서 <selectKey> 태그를 이용해서 생성된
				// boardNo가 inputBoard에 저장된 상태 == 얕은 복사된 상태
				int boardNo = inputBoard.getBoardNo();
				
				return boardNo;
		}
	
	
	@Override
	public int boardUpdate(Board inputBoard) throws Exception {
		
		// 1. 게시글 부분(제목/내용) 수정
		int result = mapper.boardUpdate(inputBoard);
		
		// 수정 실패 시 바로 리턴
		if(result == 0) return 0;
		
		return result;
	}
	
	// 게시글 삭제 서비스
	@Override
	public int boardDelete(Map<String, Integer> map) {
		return mapper.boardDelete(map);
	}
	

}
