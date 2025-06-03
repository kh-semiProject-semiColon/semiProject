package kr.co.semi.common.interceptor.model.mapper;

import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UserSessionUpdateMapper {

	int update(int memberNo);

	int studyCount(int memberNo);

}
