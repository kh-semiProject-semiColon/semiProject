package kr.co.semi.studyboard.model.service;

import java.util.Map;

public interface StudyBoardService {

	Map<String, Object> searchList(Map<String, Object> paraMap, int cp);

	Map<String, Object> selectStudyBoardList(int studyNo, int cp);



}
