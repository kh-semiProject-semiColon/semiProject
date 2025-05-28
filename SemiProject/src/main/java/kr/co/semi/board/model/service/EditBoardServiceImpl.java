package kr.co.semi.board.model.service;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
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
	

}
