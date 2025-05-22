package kr.co.semi.studyboard.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Study {
	
	private int studyNo;
	private String studyName;
	private String studyDelFl;
	private int studyMaxCount;
	private String studyDate;
	private int studyType;
	private int studyPeriod;
	private String studyMainImg;
	

}
