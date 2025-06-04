package kr.co.semi.common.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.co.semi.common.interceptor.model.service.UserSessionUpdate;
import kr.co.semi.member.model.dto.Member;

@Component
public class SessionUpdateInterceptor implements HandlerInterceptor {
	
	@Autowired
    private UserSessionUpdate UserSessionUpdate;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
	        throws Exception {

	    HttpSession session = request.getSession(false);
	    
	    if (session != null) {
	    	Member sessionUser = (Member) session.getAttribute("loginMember");

	        if (sessionUser != null) {
	        	
	        	// 해당 회원이 스터디에 가입된 수가 하나라도 있으면
	        	int countStudy = UserSessionUpdate.studyCount(sessionUser.getMemberNo());
	        	
	        	if(countStudy > 0) {
	        		
	        		sessionUser.setStudyNo(UserSessionUpdate.update(sessionUser.getMemberNo())); 
	        	}
	        	
	            // DB에서 최신 사용자 정보 조회 (권한, 상태 등)
	        			
	            // DB의 최신 정보를 기반으로 세션도 갱신
	            session.setAttribute("loginMember", sessionUser);
	            
	            if(sessionUser.getStudyNo() == 0) {
	            	// 스터디 없을 시 메시지 출력
		            session.setAttribute("message", "스터디 가입 후 이용해주세요.");
		            // 스터디 없는 상태 → 메인 페이지로 리디렉트
		            response.sendRedirect("/");
	            }

	        }
	    }

	    return true;
	}

}
