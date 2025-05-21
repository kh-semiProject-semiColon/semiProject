package kr.co.semi.board.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Calendar {
        private Long calendarNo;
        private String title;
        private String start1;
        private String end;
        private int memberNo;
        private int studyNo;
        private String color;
}