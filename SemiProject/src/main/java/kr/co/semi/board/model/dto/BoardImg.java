package kr.co.semi.board.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardImg {

	private int imgNo;						// 이미지 번호
	private String imgOriginalName; 		// 이미지 원본명
	private String imgRename;				// 이미지 개명
	private String imgPath;					// 이미지 경로
	
	
	private int boardNo;		
	
	
}
