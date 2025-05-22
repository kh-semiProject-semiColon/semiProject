package kr.co.semi.studyboard.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 🧾 StudyBoard DTO
 * 📌 STUDY_BOARD 테이블과 매핑되는 데이터 객체
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudyBoard {

    private int studyBoardNo;       // 게시글 번호
    private String studyBoardTitle; // 제목
    private String studyBoardContent; // 내용
    private Date studyBoardWriteDate; // 작성일
    private Date studyBoardUpdateDate; // 수정일
    private int readCount;          // 조회수
    private char studyBoardDelFl;   // 삭제 여부
    private int memberNo;           // 작성자 회원 번호
    private int studyNo;            // 소속 스터디 번호

    // 선택적으로 닉네임, 좋아요 수 등도 포함 가능
    private String memberNickname;  // 작성자 닉네임
    private int likeCount;          // 좋아요 수
}
