package kr.co.semi.studyboard.model.dto;

import java.sql.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Study {

	// StudyBoard 컬럼
	private int studyNo; // 스터디 고유번호 pk
	private String studyName; //스터디 이름
	private String studyDelFl; // 스터디 활성 여부 y:n
	private int studyMaxCount; // 스터디 맥스 인원수
	private String studyDate; //스터디 생성일
	private int studyType; //스터디 방향성
	private int studyPeriod; // 스터디운영기간 기본단위:개월 0 종강까지
	private String studyMainImg;
	
}
