package kr.co.semi.common.interceptor.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.semi.common.interceptor.model.mapper.UserSessionUpdateMapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserSessionUpdateImpl implements UserSessionUpdate{

	@Autowired
	private UserSessionUpdateMapper mapper;
	
	@Override
	public int update(int memberNo) {
		return mapper.update(memberNo);
	}

}
